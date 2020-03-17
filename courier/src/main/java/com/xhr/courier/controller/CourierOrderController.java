package com.xhr.courier.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xhr.courier.model.CourierOrder;
import com.xhr.courier.model.User;
import com.xhr.courier.service.CourierOrderService;
import com.xhr.courier.tools.interceptor.CurrentUser;
import com.xhr.courier.tools.interceptor.LoginRequired;
import com.xhr.courier.tools.result.Result;
import com.xhr.courier.tools.result.ResultGenerator;
import com.xhr.courier.tools.result.TableData;

@RestController
@RequestMapping("/courierOrder")
public class CourierOrderController {

	@Autowired
	private CourierOrderService courierOrderService;

	// 获取配置文件的信息
	// 用户APPid
	@Value("${alipay.appId}")
	private String app_id;

	// 私钥
	@Value("${alipay.merchantPrivateKey}")
	private String private_key;

	// 异步返回地址
	@Value("${alipay.notifyUrl}")
	private String notify_url;

	// 支付网端
	@Value("${alipay.gatewayUrl}")
	private String url;

	// 编码格式
	@Value("${alipay.charset}")
	private String charset;

	// 传输格式
	@Value("${alipay.format}")
	private String format;

	// 公钥
	@Value("${alipay.alipayPublicKey}")
	private String public_key;

	// 签名方式
	@Value("${alipay.signType}")
	private String signtype;

	/**
	 * 添加
	 * 
	 * @param courierOrder
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@LoginRequired
	public Result addCourierOrder(@RequestParam(required = false) Map<String, Object> map, @CurrentUser User user)
			throws IllegalAccessException, InvocationTargetException, ParseException {
		CourierOrder courierOrder = new CourierOrder();
		String cabinetTime = map.get("cabinetTime").toString();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		map.put("cabinetTime", simpleDateFormat.parse(cabinetTime));
		map.put("userId", user.getId());
		BeanUtils.populate(courierOrder, map);
		return ResultGenerator.genSuccessResult(courierOrderService.insertCorier(courierOrder));
	}

	/**
	 * 修改
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@LoginRequired
	public Result updateCourierOrder(@RequestParam(required = false) Map<String, Object> map) {
		return ResultGenerator.genSuccessResult(courierOrderService.updateCorier(map));
	}

	/**
	 * 接单
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/accept/{id}", method = RequestMethod.GET)
	@LoginRequired
	public Result updateCourierOrder(@PathVariable(value = "id") String id, @CurrentUser User user) {

		if (user.getAuth().equals("0")) {
			return ResultGenerator.genFailResult("接单失败，你暂未通过审核");
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);

		CourierOrder findById = courierOrderService.findById(Integer.parseInt(id));
		if (findById.getStatus().equals("1") || findById.getStatus().equals("2")) {
			return ResultGenerator.genFailResult("该订单已被接取");
		}

		map.put("orderUserId", user.getId());
		map.put("status", 1);
		map.put("acceptTime", new Date());
		return ResultGenerator.genSuccessResult(courierOrderService.updateCorier(map));
	}

	/**
	 * 条件查询
	 * 
	 * @param map
	 * @param pageNum
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/findList", method = RequestMethod.GET)
	public Result findList(@RequestParam(required = false) Map<String, Object> map, Integer pageNum, Integer size) {
		Page<Map<String, Object>> page = PageHelper.startPage(pageNum == null ? 1 : pageNum, size == null ? 5 : size);
		List<Map<String, Object>> list = courierOrderService.findList(map);
		return ResultGenerator.genSuccessResult(new TableData<Map<String, Object>>(page.getTotal(), list));
	}

	/**
	 * 条件查询(首页)
	 * 
	 * @param map
	 * @param pageNum
	 * @param size
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/findListIndex", method = RequestMethod.GET)
	public Result findListIndex(@RequestParam(required = false) Map<String, Object> map, Integer pageNum, Integer size)
			throws ParseException {
		Page<Map<String, Object>> page = PageHelper.startPage(pageNum == null ? 1 : pageNum, size == null ? 5 : size);
		List<Map<String, Object>> list = courierOrderService.findListIndex(map);
		return ResultGenerator.genSuccessResult(new TableData<Map<String, Object>>(page.getTotal(), list));
	}

	/**
	 * id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public Result findList(@PathVariable(value = "id") Integer id) {
		if (id == null) {
			return ResultGenerator.genFailResult("参数错误");
		}
		return ResultGenerator.genSuccessResult(courierOrderService.findById(id));
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public Result delete(@PathVariable(value = "id") Integer id) {
		if (id == null) {
			return ResultGenerator.genFailResult("参数错误");
		}
		return ResultGenerator.genSuccessResult(courierOrderService.deleteCour(id));
	}

	/**
	 * 支付宝h5支付 orderId
	 * 
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/alipayH5Pay/{id}", method = RequestMethod.GET)
	public void alipayH5Pay(@PathVariable(value = "id") String id, HttpServletResponse response) throws IOException {
		CourierOrder findById = courierOrderService.findById(Integer.parseInt(id));
		// 超时时间 可空
		String timeout_express = "1h";
		// 销售产品码 必填
		String product_code = "QUICK_WAP_WAY";
		/**********************/
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
		// 调用RSA签名方式
		AlipayClient client = new DefaultAlipayClient(url, app_id, private_key, format, charset, public_key, signtype);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(findById.getOrderId() + "");
		model.setSubject("代领费用");
		model.setTotalAmount(findById.getPrice());
		model.setBody("代领费用");
		model.setTimeoutExpress(timeout_express);
		model.setProductCode(product_code);
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(notify_url);
		// form表单生产
		String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
			response.setContentType("text/html;charset=" + charset);
			response.getWriter().write(form);// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 支付宝服务器异步通知
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("/notifyUrl")
	public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		Map<String, Object> map = new HashMap<>();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			valueStr = new String(valueStr);
			params.put(name, valueStr);
		}

		boolean signVerified = AlipaySignature.rsaCheckV1(params, public_key, charset, signtype); // 调用SDK验证签名

		if (signVerified) { // 验证成功 更新订单信息
			System.out.println("异步通知成功");
			// 商户订单号
			String out_trade_no = request.getParameter("out_trade_no");
			map.put("orderId", out_trade_no);
			List<Map<String,Object>> findList = courierOrderService.findList(map);
			String id = null;
			if (findList.size() > 0) {
				id = findList.get(0).get("id").toString();
			}
			// 修改数据库
			map.put("id", id);
			map.put("payStatus", "1"); // 已支付
			courierOrderService.updateCorier(map);
		} else {
			System.out.println("异步通知失败");
		}
	}

	/**
	 * 条件查询
	 * 
	 * @param map
	 * @param pageNum
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "/admin/findList", method = RequestMethod.GET)
	@LoginRequired
	public Result adminFindList(@RequestParam(required = false) Map<String, Object> map, Integer limit, Integer page) {
		Page<Map<String, Object>> pageBean = PageHelper.startPage(page == null ? 1 : page, limit == null ? 5 : limit);
		List<Map<String, Object>> list = courierOrderService.findList(map);
		return ResultGenerator.genSuccessResult(new TableData<Map<String, Object>>(pageBean.getTotal(), list));
	}

	/**
	 * 统计
	 * 
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/admin/statics")
	public Result statics() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> findList = courierOrderService.findList(map);
		Map<String, Object> resMap = new HashMap<String, Object>();
		// 订单总数
		resMap.put("orderTotal", findList.size());
		map.put("status", "2");
		List<Map<String, Object>> findList2 = courierOrderService.findList(map);
		// 订单完成数
		resMap.put("orderSuccessTotal", findList2.size());
		double total = 0;
		for (Map<String, Object> map2 : findList2) {
			double parseDouble = Double.parseDouble(map2.get("price").toString());
			total += parseDouble;
		}
		// 交易额
		resMap.put("total", total);
		return ResultGenerator.genSuccessResult(resMap);
	}
}
