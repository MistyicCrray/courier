package com.xhr.courier.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.xhr.courier.dao.UserMapper;
import com.xhr.courier.model.User;
import com.xhr.courier.tools.FileUtil;
import com.xhr.courier.tools.MD5;
import com.xhr.courier.tools.SendEmailUtil;
import com.xhr.courier.tools.ServiceException;
import com.xhr.courier.tools.TokenUtil;
import com.xhr.courier.tools.UUIDUtils;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	// 读取配置文件中的参数
	@Value("${spring.mail.username}")
	private String form;

	@Value("${local_url}")
	private String domain;

	@Autowired
	private JavaMailSender javaMailSender;

	public User findById(int id) {
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 用户注册
	 * 
	 * @Title: addUser
	 * @Description: TODO
	 * @param user
	 * @param file
	 * @return String
	 */
	public User add(User user) {
		String password = user.getPassword();
		user.setPassword(MD5.md5(password));
		user.setCreateTime(new Date());

		// 检测用户登录账号是否存在
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loginName", user.getLoginName());
		List<User> list = userMapper.findList(map);
		if (list.size() != 0) {
			return null;
		}
		userMapper.insert(user);
		return user;
	}

	/**
	 * 登录
	 * 
	 * @Title: login
	 * @Description: TODO
	 * @param map
	 * @return Map<String,Object>
	 */
	public Map<String, Object> login(Map<String, Object> map) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		User user = userMapper.findList(map).get(0);
		user.setPassword("");
		resultMap.put("userInfo", user); // 存入user信息
		resultMap.put("accessToken", TokenUtil.createJwtToken(user.getId() + "")); // 存入token
		return resultMap;
	}

	public List<User> findList(Map<String, Object> map) {
		return userMapper.findList(map);
	}

	public void update(Map<String, Object> userMap, MultipartFile file) {
		if (file != null) {
			userMap.put("avatar", (String) FileUtil.uploadImage(file).get("filePath"));
		}
		userMapper.update(userMap);
	}

	/**
	 * 发送邮件
	 * 
	 * @param user
	 * @throws MessagingException
	 */
	public void sendEmail(String email) throws MessagingException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		List<User> findList = userMapper.findList(map);
		if (findList.size() != 0) {
			User user = findList.get(0);
			String activeCode = UUIDUtils.getActiveCode();
			SendEmailUtil.send(activeCode, user.getEmail(), "验证码", javaMailSender, form);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", user.getId());
			userMap.put("emailCode", activeCode);
			userMapper.update(userMap);
		} else {
			throw new ServiceException("邮箱不存在");
		}
	}

}
