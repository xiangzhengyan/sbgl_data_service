package com.zfy.sbgl.collection.util;

import java.util.HashSet;

/**
 * @author xiangzy
 * @date 2015-9-12
 *
 */
public class MsgType {
	
//	$login,设备序列号,密码*校验值

//  $Query,PreData,整机输出功率,整机反射功率,AGC电压,前级输入功率,前级输出电平,系统温度,衰减值,整机反射门限值,整机输入下限,整机输入上限,PowData,功放模块数量,功放下标,功放功率,反射功率,50V电压,功放1电流值,功放1温度值,功放2电流值,功放2温度值 ,PowData,功放模块数量,功放下标,功放功率,反射功率,50V电压,功放1电流值,功放1温度值,功放2电流值,功放2温度值 ,SetPowData,风扇启动温度,告警温度值,告警电流值,告警反射值*x
// $Query,PreData,1,2,3,4,5,6,7,8,9,10,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9 ,SetPowData,1,2,3,4*x
//	$Heartbeat,?*校验值
	
	//未知 服务器发送 
	public static final String unknow = "unknow";
	public static final String exception = "exception";
	
	//设备主动发送
	public static final String login = "login";
    public static final String Heartbeat = "Heartbeat";//
    public static final String Gap = "Gap";
    public static final String Alarm = "Alarm";
    //服务器发送、设备返回
    public static final String  Query = "Query";
    public static final String SetAll = "SetAll";//
    
    //web发送的设置命令
    public static final String SetAllFromWeb ="SetAllFromWeb";
    public static final String QueryFromWeb ="QueryFromWeb";
    public static final String GetHeartFromWeb ="GetHeartFromWeb";
    
    private static HashSet<String> upTypeSet = new HashSet<String>();
    
    static{
    	 //设备发送
    	upTypeSet.add(login);
    	upTypeSet.add(Heartbeat);
    	upTypeSet.add(Gap);
    	upTypeSet.add(Alarm);
    	//服务器发送、设备返回设备返回
    	upTypeSet.add(SetAll);
    	upTypeSet.add(Query);
    	
    	
    }
    
    public static boolean isUpType(String type){
    	return upTypeSet.contains(type);
    }


}
