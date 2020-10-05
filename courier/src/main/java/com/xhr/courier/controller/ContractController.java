package com.xhr.courier.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xhr.courier.model.User;
import com.xhr.courier.service.ContractService;
import com.xhr.courier.tools.interceptor.CurrentUser;
import com.xhr.courier.tools.interceptor.LoginRequired;
import com.xhr.courier.tools.result.Result;
import com.xhr.courier.tools.result.ResultGenerator;
import com.xhr.courier.tools.result.TableData;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	private ContractService contractService;
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@LoginRequired
	public Result addContract(@RequestBody Map<String, Object> map, @CurrentUser User curUser) {
		map.put("userId", curUser.getId());
		contractService.addContract(map);
		return ResultGenerator.genSuccessResult("添加成功");
	}

	@RequestMapping(value = "/find", method = RequestMethod.GET)
	@LoginRequired
	public Result findContract(@CurrentUser User currentUser, Integer pageNum, Integer size) {
		Page<Map<String, Object>> page = PageHelper.startPage(pageNum == null ? 1 : pageNum, size == null ? 5 : size);
		List<Map<String, Object>> list = contractService.findByUserId(currentUser.getId());
		return ResultGenerator.genSuccessResult(new TableData<Map<String, Object>>(page.getTotal(), list));
	}

	@RequestMapping(value = "/findContractInfo", method = RequestMethod.GET)
	@LoginRequired
	public Result findContractInfo(@CurrentUser User currentUser, Integer pageNum, Integer size, int toUserId) {
		Page<Map<String, Object>> page = PageHelper.startPage(pageNum == null ? 1 : pageNum, size == null ? 5 : size);
		List<Map<String, Object>> list = contractService.findByUserIdContractInfo(currentUser.getId(), toUserId);
		return ResultGenerator.genSuccessResult(new TableData<Map<String, Object>>(page.getTotal(), list));
	}
}
