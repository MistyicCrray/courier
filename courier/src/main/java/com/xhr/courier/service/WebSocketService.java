package com.xhr.courier.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.xhr.courier.model.User;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 *                 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/websocket/{userId}")
@Component
public class WebSocketService {
	// 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;
	private String userId;
	private List<User> users = new ArrayList<>();
	// 存储当前用户，单个消息发送
	private static Map<String, Session> map = new HashMap<>();
	// 存储用户，群发消息发送
	// concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static ConcurrentHashMap<String, WebSocketService> webSocketSet = new ConcurrentHashMap<String, WebSocketService>();

	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;

	static Log log = LogFactory.getLog(WebSocketService.class);

	/**
	 * 连接建立成功调用的方法
	 * 
	 * @param session
	 *            可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 * @throws UnsupportedEncodingException
	 */
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session) throws UnsupportedEncodingException {
		System.out.println(userId);
		this.session = session;
		// 解决乱码问题
		User user = new User();
		user.setId(Integer.parseInt(userId));
		users.add(user);
		// 存储用户信息 session为用户发送信息对象
		webSocketSet.put(userId, this); // 加入set中
		addOnlineCount(); // 在线数加1
		System.out.println("有新连接加入！" + userId + "当前在线人数为" + getOnlineCount());
	}

	/**
	 * 连接关闭调用的方法
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@OnClose
	public void onClose() throws UnsupportedEncodingException {
		webSocketSet.remove(this); // 从set中删除
		subOnlineCount(); // 在线数减1
		Collection<Session> values = map.values();
		values.remove(this);
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 * @param session
	 *            可选的参数
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		if (StringUtil.isNotEmpty(message)) {
			try {
				// 解析发送的报文
				JSONObject jsonObject = JSON.parseObject(message);
				// 追加发送人(防止串改)
				jsonObject.put("fromUserId", this.userId);
				String toUserId = jsonObject.getString("toUserId");
				// 传送给对应toUserId用户的websocket
				if (StringUtil.isNotEmpty(toUserId) && webSocketSet.containsKey(toUserId)) {
					webSocketSet.get(toUserId).sendMessage(jsonObject.toJSONString());
				} else {
					log.error("请求的userId:" + toUserId + "不在该服务器上");
					// 否则不在这个服务器上，发送到mysql或者redis
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发生错误时调用
	 * 
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
		log.info("发送消息到:" + userId + "，报文:" + message);
		if (StringUtil.isNotEmpty(userId) && webSocketSet.containsKey(userId)) {
			webSocketSet.get(userId).sendMessage(message);
		} else {
			log.error("用户" + userId + ",不在线！");
		}
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocketService.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocketService.onlineCount--;
	}

}
