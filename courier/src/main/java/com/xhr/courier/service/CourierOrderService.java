package com.xhr.courier.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xhr.courier.dao.CourierOrderMapper;
import com.xhr.courier.model.CourierOrder;

@Service
public class CourierOrderService {

	@Autowired
	private CourierOrderMapper courierOrderMapper;

	public int insertCorier(CourierOrder courierOrder) {
		courierOrder.setCreateTime(new Date());
		courierOrder.setStatus("0");
		courierOrder.setPayStatus("0");
		if (courierOrder.getIsBig().equals("0")) {
			courierOrder.setPrice("0.8");
		} else {
			courierOrder.setPrice("1.6");
		}
		return courierOrderMapper.insert(courierOrder);
	}

	public int updateCorier(Map<String, Object> map) {
		if (map.get("isBig") != null) {
			if (map.get("isBig").toString().equals("0")) {
				 map.put("price", "0.8");
			} else {
				map.put("price", "1");
			}
		}
		return courierOrderMapper.update(map);
	}

	public List<Map<String, Object>> findList(Map<String, Object> map) {
		return courierOrderMapper.findList(map);
	}

	public CourierOrder findById(int id) {
		return courierOrderMapper.selectByPrimaryKey(id);
	}

	public int deleteCour(int id) {
		return courierOrderMapper.deleteByPrimaryKey(id);
	}
}
