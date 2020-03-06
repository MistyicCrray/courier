package com.xhr.courier.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.xhr.courier.model.User;
import com.xhr.courier.service.UserService;
import com.xhr.courier.tools.MD5;
import com.xhr.courier.tools.interceptor.CurrentUser;
import com.xhr.courier.tools.interceptor.LoginRequired;
import com.xhr.courier.tools.result.Result;
import com.xhr.courier.tools.result.ResultGenerator;
import com.xhr.courier.tools.result.TableData;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public Result findById(@PathVariable(name = "id") String id) {
		User findById = userService.findById(Integer.parseInt(id));
		findById.setPassword("");
		return ResultGenerator.genSuccessResult(findById);
	}

	/**
	 * 用户注册
	 * 
	 * @Title: insertUser
	 * @Description: TODO
	 * @param user
	 * @param file
	 * @return Result
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public Result insertUser(@RequestParam(required = false) Map<String, Object> map)
			throws IllegalAccessException, InvocationTargetException {
		User user = new User();
		BeanUtils.populate(user, map);
		User add = userService.add(user);
		if (add == null) {
			return ResultGenerator.genFailResult("账号已被注册");
		}
		return ResultGenerator.genSuccessResult(add);
	}

	/**
	 * 登录
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Result Login(@RequestParam(required = false) Map<String, Object> map) {
		map.put("password", MD5.md5(map.get("password").toString()));
		List<User> users = userService.findList(map);
		if (users.size() == 0) {
			return ResultGenerator.genFailResult("用户名或密码错误");
		}
		return ResultGenerator.genSuccessResult(userService.login(map));
	}

	/**
	 * 更换头像
	 * 
	 * @param img
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/changeAvatar", method = RequestMethod.POST)
	public Result changeImg(@RequestParam(required = false) MultipartFile img, @CurrentUser User user) {
		System.out.println(img);
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("id", user.getId());
		userService.update(userMap, img, null);
		return ResultGenerator.genSuccessResult("修改成功");
	}

	/**
	 * 更换学生证
	 * 
	 * @param img
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/changeStudentIdCard", method = RequestMethod.POST)
	public Result changeStudentIdCard(@RequestParam(required = false) MultipartFile img, @CurrentUser User user) {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("id", user.getId());
		userService.update(userMap, null, img);
		return ResultGenerator.genSuccessResult("修改成功");
	}

	/**
	 * 根据旧密码修改密码
	 * 
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public Result resetPassword(@RequestParam(required = false) Map<String, String> map, @CurrentUser User user) {
		String md5 = MD5.md5(map.get("oldPassword"));
		if (!user.getPassword().equals(md5)) {
			return ResultGenerator.genFailResult("旧密码输入错误");
		} else {
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("password", MD5.md5(map.get("passwordOne")));
			userMap.put("id", user.getId());
			userService.update(userMap, null, null);
		}
		return ResultGenerator.genSuccessResult("修改成功");
	}

	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/curUser", method = RequestMethod.GET)
	public Result resetPassword(@CurrentUser User user) {
		user.setPassword("");
		return ResultGenerator.genSuccessResult(user);
	}

	/**
	 * 修改用户信息
	 * 
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/updateCurUser", method = RequestMethod.GET)
	public Result update(@RequestParam(required = false) Map<String, Object> map, @CurrentUser User user) {
		map.put("id", user.getId());
		userService.update(map, null, null);
		return ResultGenerator.genSuccessResult("修改成功");
	}

	/**
	 * 发送邮件
	 * 
	 * @return
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/sendEmail/{email}", method = RequestMethod.GET)
	public Result sendEmail(@PathVariable String email) throws MessagingException {
		userService.sendEmail(email);
		return ResultGenerator.genSuccessResult("发送成功");
	}

	/**
	 * 忘记密码
	 * 
	 * @param user
	 * @return
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/emailUpdatePwd", method = RequestMethod.GET)
	public Result emailUpdatePwd(@RequestParam(required = false) Map<String, String> map) {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("email", map.get("email"));
		userMap.put("emailCode", map.get("emailCode"));
		List<User> findList = userService.findList(userMap);
		if (findList.size() > 0) {
			User user = findList.get(0);
			userMap.put("password", MD5.md5(map.get("passwordOne")));
			userMap.put("id", user.getId());
			// 置空
			userMap.put("emailCode", "");
			userService.update(userMap, null, null);
			return ResultGenerator.genSuccessResult("修改成功");
		} else {
			return ResultGenerator.genFailResult("验证码不正确");
		}
	}

	/**
	 * 管理员查询
	 * 
	 * @param map
	 * @param user
	 * @param pageNum
	 * @param size
	 * @return
	 */
	@LoginRequired
	@RequestMapping(value = "/admin/userList", method = RequestMethod.GET)
	public Result findUserList(@RequestParam(required = false) Map<String, Object> map, @CurrentUser User user,
			Integer page, Integer limit) {
		if (user.getUserType().equals("1")) {
			return ResultGenerator.genFailResult("权限不足");
		}
		Page<User> pageBean = PageHelper.startPage(page == null ? 1 : page, limit == null ? 5 : limit);
		List<User> list = userService.findList(map);
		return ResultGenerator.genSuccessResult(new TableData<User>(pageBean.getTotal(), list));
	}

	/**
	 * 管理员审核
	 * 
	 * @return
	 * @throws MessagingException
	 */
	@RequestMapping(value = "/admin/userAuth/{userId}", method = RequestMethod.POST)
	@LoginRequired
	public Result userAuth(@CurrentUser User user, @PathVariable int userId) {
		if (user.getUserType().equals("1")) {
			return ResultGenerator.genFailResult("权限不足");
		}
		User findById = userService.findById(userId);
		if (findById.getAuth().equals("1")) {
			return ResultGenerator.genFailResult("用户已审核通过");
		}
		if (StringUtil.isNotEmpty(findById.getStudentIdCard())) {
			return ResultGenerator.genFailResult("用户暂未上传学生证");
		}
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("auth", 1);
		userService.update(userMap, null, null);
		return ResultGenerator.genSuccessResult("审核成功");
	}

}
