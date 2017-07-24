package com.zfy.sbgl.collection.util;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiangzy
 * @date 2015-9-12
 *
 */
public class ChannelUtil {
	

	
	private static Map<Integer,Channel>idChannelMap = new HashMap<Integer, Channel>();
	
	private static Map<String,Integer>codeIdMap = new HashMap<String, Integer>();
	
	private static Map<Integer,String> idCodeMap = new HashMap<Integer, String>();
	

	
	public static void putChannel(String code,Channel channel){
		Integer id = channel.hashCode();
		idChannelMap.put(id, channel);
		codeIdMap.put(code, id);
		idCodeMap.put(id, code);
	}
	
	public static boolean isLoginChanel(Integer id){
		return idChannelMap.containsKey(id);
	}
	
	public static Channel getChannel(String code){
		Integer id = codeIdMap.get(code);
		if(id!=null){
			return idChannelMap.get(id);
		}
		return null;
	}
	
	public static String getCode(Integer id){
		return idCodeMap.get(id);
	}
		
	
	public static void removeChannel(Integer id){
		idChannelMap.remove(id);
		String code = idCodeMap.remove(id);
		codeIdMap.remove(code);
	}
	

	
	
}
