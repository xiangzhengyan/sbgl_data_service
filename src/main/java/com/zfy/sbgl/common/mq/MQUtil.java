/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfy.sbgl.common.mq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zfy.sbgl.collection.cfg.Config;

/**
 * @author xiangzy
 * @date 2014-12-30
 * 
 */
public class MQUtil {


	
	private static Channel broadcastChannel;
	
	private static Gson gson = new Gson();

	private static Logger logger = Logger.getLogger(MQUtil.class);
	static {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(Config.mqHost);
			factory.setPort(Config.mqPort);
			factory.setUsername(Config.mqUsername);
			factory.setPassword(Config.mqPassword);
			factory.setVirtualHost(Config.mqVirtualHost);

			Connection connection = factory.newConnection();

			broadcastChannel = connection.createChannel();
			broadcastChannel.exchangeDeclare(MQ.EXCHANGE_BROADCAST, "fanout");

		} catch (Exception e) {
			logger.error("MQ初始化异常", e);
		}
	}

	public static void broadcast(BaseMesssage messsage) throws IOException {
		if (broadcastChannel == null || !broadcastChannel.isOpen()) {
			return;
		}
		broadcastChannel.basicPublish(MQ.EXCHANGE_BROADCAST, "", null,
					gson.toJson(messsage).getBytes());
	}


}
