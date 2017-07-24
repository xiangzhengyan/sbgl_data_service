/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package test;

import com.zfy.sbgl.collection.util.StringUtil;

/**
 * @author xiangzy
 * @date 2014-12-25
 * 
 */
public class ClientSendOnce {

	/**
	 * @param args
	 * @throws InterruptedException
	 *             "116.55.245.125
	 */
	public static void main(String[] args) throws Exception {
		// $login,设备序列号,密码*校验值
		// $PreData,整机输出功率,整机反射功率,AGC电压,前级输入功率,前级输出电平,系统温度,衰减值,整机反射门限值,整机输入下限,整机输入上限*校验值<CR><LF>
		// $PowData,功放模块数量,功放下标,功放功率,反射功率,50V电压,功放1电流值,功放1温度值,功放2电流值,功放2温度值*校验值
		// $SetPowData,风扇启动温度,告警温度值,告警电流值,告警反射值*校验值<CR><LF>
		// $Heartbeat,?*校验值
		// 成功:$SetPowFan,OK*校验值<CR><LF>
		// 错误:$SetPowFan,ERROR*校验值<CR><LF>

		// "$GPGGA,085014.955,2839.2050,N,11549.5721,E,1,04,03.6,76.6,M,-6.2,M,,*4C

		String str_login = "login,DVB-T-615-U-1000W-I-1509011010,pass";

		String str = str_login;
		String ip1 = "120.25.69.45";
		String ip2 = "localhost";

		Client client = new Client(ip1, 18888);
		client.connect();

		client.write(getMsg(str));
		System.out.println(client.read());

		char b = 1;
//		client.write("$Heartbeat,"+b+"*x!");
		client.write(getMsg("Query,PreData,"+(char)(99)+","+b+",3,3,3,1,"+(char)(15)+",1,1,0,PowData,3,3,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,PowData,1,2,3,4,5,6,7,8,9,SetPowData,1,2,3,4"));
		String rs = client.read();
		System.out.println(rs);
//		for (int i = 1; i < 30; i++) {
//			
//			Thread.sleep(1000);
//			client.write(getMsg("SetPowData,"+i+"1,"+i+"2,"+i+"3,"+i+"4"));
//			client.read();
//			client.write(getMsg("PreData,"+i+"1,"+i+"2,"+i+"3,"+i+"4,"+i+"5,"+i+"6,"+i+"7,"+i+"8,"+i+"9,"+i+"0"));
//			client.read();
//			client.write(getMsg("PowData,"+i+"1,"+i+"2,"+i+"3,"+i+"4,"+i+"5,"+i+"6,"+i+"7,"+i+"8,"+i+"9"));
//			client.read();
//		}
//
		client.close();

	}

	public static String getMsg(String str) {
		String xor = StringUtil.toXor(str);

		String msg = "$" + str + "*" + xor + '!';
		System.out.println(msg);
		return msg;

	}

}
