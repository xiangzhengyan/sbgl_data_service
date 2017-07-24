package com.zfy.sbgl.collection.util;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.zfy.sbgl.collection.db.MySQLDBService;

/**
 * @author xiangzy
 * @date 2015-9-24
 * 
 */
public class HeartUtil {

	private static Map<String, Heart> heartMap = new HashMap<String, Heart>();
	private static Timestamp lastTime;

	public static void startSaveTask() {
		java.util.Timer timer = new java.util.Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				saveHeart();
				lastTime = null;
			}
		}, 0, 3000);
	}

	private static void saveHeart() {
		if(lastTime==null){
			return;
		}
		if(System.currentTimeMillis() - lastTime.getTime()>20000){
			//20秒之前的更新，不处理
			return;
		}
		try {
			MySQLDBService.getInstance().saveHeart(HeartUtil.heartMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void putHeart(String code, int status) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		heartMap.put(code,new Heart(time, status) );
		lastTime= time;
	}

	public static Heart getHeart(String code) {
		return heartMap.get(code);
	}



}
