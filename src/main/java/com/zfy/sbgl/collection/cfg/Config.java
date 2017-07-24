/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfy.sbgl.collection.cfg;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author xiangzy
 * @date 2014-12-25
 * 
 */
public class Config {

	private static final Logger logger = Logger.getLogger(Config.class);
	private static Properties prop = new Properties();
	
	public static String env;
	
	public static String version;
	
	public static int serverPort;

	public static String dbDriver;
	public static String dbUrl;
	public static String dbUsername;
	public static String dbPassword;
	
	public static String mqHost;
	public static int mqPort;
	public static String mqUsername;
	public static String mqPassword;
	public static String mqVirtualHost;
	 
	
	static {
		
		try {
			
			String e = System.getenv("CAMEL_ENV");
			if(e==null){
				e = "development";
			}
			env  = e;
			
			
			prop.load(Config.class.getResourceAsStream("/config.properties"));
	
			version = prop.getProperty("version");
			
			serverPort = Integer.parseInt(getConfig("server.port"));
			
			
			dbDriver = getConfig("database.driver");
			dbUrl = getConfig("database.url");
			dbUsername = getConfig("database.username");
			dbPassword = getConfig("database.password");
	
			
			mqHost = getConfig("mq.host");
			mqPort = Integer.parseInt(getConfig("mq.port","5672"));
			mqUsername = getConfig("mq.username");
			mqPassword = getConfig("mq.password");
			mqVirtualHost = getConfig("mq.virtualHost");
			

		} catch (Exception e) {
			logger.error("读取配置文件错误", e);
		}
	}
	
	public static String getConfig(String key){
		return prop.getProperty(env+"."+key);
	}


	public static String getConfig(String key,String defaultValue){
		return prop.getProperty(env+"."+key,defaultValue);
	}

	


}
