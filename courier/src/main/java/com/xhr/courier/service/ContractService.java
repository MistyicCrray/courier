package com.xhr.courier.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.courier.dao.ContractContentMapper;
import com.xhr.courier.dao.ContractMapper;
import com.xhr.courier.model.Contract;
import com.xhr.courier.model.ContractContent;

@Service
public class ContractService {

	@Autowired
	private ContractMapper contractMapper;

	@Autowired
	private ContractContentMapper contractContentMapper;

	/**
	 * 添加聊天人信息（发送者id以及接收者id）
	 * 
	 * @param map
	 * @return
	 */
	public int addContract(Map<String, Object> map) {

		// 查询是否存在联系人信息
		Contract contract = contractMapper.findByUserIdAndToUserId(map);
		// 不存在则新增信息
		if (contract == null) {
			contract = new Contract();
			contract.setCreateTime(new Date());
			contract.setToUserId(Integer.parseInt(map.get("toUserId").toString()));
			contract.setUserId(Integer.parseInt(map.get("sendUserId").toString()));
			contractMapper.insert(contract);
		}
		// 创建聊天内容
		ContractContent contractContent = new ContractContent();
		contractContent.setContent(map.get("content").toString());
		contractContent.setContractId(contract.getId());
		contractContent.setSendUserId(Integer.parseInt(map.get("sendUserId").toString()));
		contractContent.setToUserId(Integer.parseInt(map.get("toUserId").toString()));
		contractContent.setCreateTime(new Date());
		return contractContentMapper.insert(contractContent);
	}

	/**
	 * 删除聊天人信息（发送者id以及接收者id）
	 * 
	 * @param id
	 * @return
	 */
	public int delContract(int id) {
		return contractMapper.deleteByPrimaryKey(id);
	}

	/**
	 * 获取聊天信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> findByUserId(int userId) {
		List<Map<String, Object>> contractList = contractMapper.findByUserId(userId);
		if (contractList.size() != 0) {
			for (Map<String, Object> map : contractList) {
				int contractId = Integer.parseInt(map.get("id").toString());
				List<Map<String, Object>> contents = contractContentMapper.findByContractIdContract(contractId);
				map.put("contents", contents);
			}
		}
		return contractList;
	}

	/**
	 * 获取聊天信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> findByUserIdContractInfo(int userId, int toUserId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("toUserId", toUserId);
		List<Map<String, Object>> contents = contractContentMapper.findByUserIdOrToUserIdContractInfo(map);
		return contents;
	}
}
