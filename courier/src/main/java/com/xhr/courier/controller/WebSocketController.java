package com.xhr.courier.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xhr.courier.model.User;
import com.xhr.courier.service.ContractService;
import com.xhr.courier.service.WebSocketService;
import com.xhr.courier.tools.interceptor.CurrentUser;
import com.xhr.courier.tools.interceptor.LoginRequired;
import com.xhr.courier.tools.result.Result;
import com.xhr.courier.tools.result.ResultGenerator;

@RestController
public class WebSocketController {

	@Autowired
	private ContractService contractService;

	@RequestMapping("/push/{toUserId}")
	@LoginRequired
	public Result pushMsg(String message, @PathVariable String toUserId, @CurrentUser User user) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("toUserId", Integer.parseInt(toUserId));
		map.put("sendUserId", user.getId());
		map.put("content", message);
		contractService.addContract(map);
		WebSocketService.sendInfo(message, toUserId);
		return ResultGenerator.genSuccessResult("发送成功");
	}
}