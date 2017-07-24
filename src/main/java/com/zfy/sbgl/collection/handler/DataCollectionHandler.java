/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfy.sbgl.collection.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

import org.apache.log4j.Logger;

import com.zfy.sbgl.collection.db.MySQLDBService;
import com.zfy.sbgl.collection.util.ChannelUtil;
import com.zfy.sbgl.collection.util.Heart;
import com.zfy.sbgl.collection.util.HeartUtil;
import com.zfy.sbgl.collection.util.MRecord;
import com.zfy.sbgl.collection.util.MsgType;
import com.zfy.sbgl.collection.util.StringUtil;

/**
 * @author xiangzy
 * @date 2014-12-22
 * 
 */
public class DataCollectionHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = Logger
			.getLogger(DataCollectionHandler.class);

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {

		logger.debug("接收数据" + ctx.channel().remoteAddress() + "\n" + msg);

		try {
			int startIndex = msg.indexOf("$");
			if (startIndex == -1 || msg.length() < 10) {
				logger.debug("非法数据0");
				this.responseERROR(ctx, "unknow");
				return;
			}
			msg = msg.substring(startIndex + 1);
			
			if (msg.startsWith(MsgType.GetHeartFromWeb)) {
				this.processGetetHeartFromWeb(ctx, msg);
				return;
			}
	
			if (msg.startsWith( MsgType.QueryFromWeb)) {
				this.processQueryFromFromWeb(ctx, msg);
				return;
			}
			
			if (msg.startsWith(MsgType.SetAllFromWeb)) {
				this.processSetAllFromWeb(ctx, msg);
				return;
			}


			String[] msgs = msg.split("\\*");
			if (msgs.length != 2 || msgs[0].length() < 8
					|| msgs[1].length() != 1) {
				// TODO
				logger.debug("非法数据1");
				this.responseERROR(ctx, MsgType.unknow);
				return;
			}
			int commaIndex = msgs[0].indexOf(",");
			if (commaIndex < 3 || commaIndex == msgs[0].length() - 1) {
				logger.debug("非法数据2");
				this.responseERROR(ctx, MsgType.unknow);
				return;
			}
			String type = msgs[0].substring(0, commaIndex);
			if (!MsgType.isUpType(type)) {
				logger.debug("非法数据类型");
				this.responseERROR(ctx, MsgType.unknow);
				return;
			}

			// String xor = StringUtil.toXor(msgs[0]);
			// if(!xor.equalsIgnoreCase(msgs[1])){
			// //TODO login 还需要返回code
			// logger.debug("校验和错误");
			// this.responseERROR(ctx, type);
			// return;
			// }

			String value = msgs[0].substring(commaIndex + 1);
			if (type.equals(MsgType.login)) {
				this.processLogin(ctx, type, value);
			} else {
				String code = ChannelUtil.getCode(ctx.channel().hashCode());
				if (code == null) {
					logger.debug("没有登陆");
					this.responseERROR(ctx, type);
					return;
				}

				if (type.equals(MsgType.Heartbeat)) {
					this.processHeartbeat(ctx,code,value);

				} else if (type.equals(MsgType.Alarm)
						|| type.equals(MsgType.Gap)
						|| type.equals(MsgType.Query)) {
					this.processData(ctx, code, type, value);

				} else if (type.equals(MsgType.SetAll)) {
					// 不做处理
				}

			}

		} catch (Exception e) {
			logger.error("处理上传数据异常", e);
			this.responseERROR(ctx, MsgType.exception);
		}
	}



	private void processHeartbeat(ChannelHandlerContext ctx, String code,
			String value) {
		//TODO 需要编码
		int status = "1".equals(value)?1:0;
		HeartUtil.putHeart(code, status);
		this.responseOK(ctx, MsgType.Heartbeat);
		
	}

	private void processLogin(ChannelHandlerContext ctx, String type, String value) {
		String logins[] = value.split(",");
		String code = logins[0];
		String pass = logins[1];
		try {
			ChannelUtil.removeChannel(ctx.channel().hashCode());
			// TODO 校验
			if (MySQLDBService.getInstance().login(code, pass)) {
				logger.debug("登陆成功");
				this.responseLoginOK(ctx, code);
				ChannelUtil.putChannel(code, ctx.channel());
				MySQLDBService.getInstance().saveLog(code, type, "1");

			} else {
				logger.debug("登陆失败");
				this.responseLoginERROR(ctx, code);
				MySQLDBService.getInstance().saveLog(code, type, "0");
			}
		} catch (Exception e) {
			logger.error("登陆异常",e);
			this.responseLoginERROR(ctx, code);
		}

	}


	private void processData(ChannelHandlerContext ctx, String code,
			String type, String value) {
		try {
			if (!value.contains("PreData") || !value.contains("PowData")
					|| !value.contains("SetPowData")) {
				logger.debug("data数据格式错误1");
				this.responseERROR(ctx, type);
				return;
			}
//			MRecord record = MRecord.parse(value);
//			if(record==null){
//				logger.debug("data数据格式错误2");
//				this.responseERROR(ctx, type);
//				return;	
//			}
//			
//			//转换成字符-begin
//			StringBuffer sbf = new StringBuffer();
//			String[] predatas = record.getPreData();
//			sbf.append("PreData");
//			for (String c : predatas) {
//				int val = (int)c.charAt(0);
//				sbf.append(",").append(val);
//			}
//			List<String[]> powdataList =  record.getPowDataList();
//			for (String[] powdatas : powdataList) {
//				sbf.append(",PowData");
//				for (String c : powdatas) {
//					int val = (int)c.charAt(0);
//					sbf.append(",").append(val);
//				}
//			}
//			sbf.append(",SetPowData");
//			String[] setpowdatas = record.getSetPowData();
//			for (String c : setpowdatas) {
//				int val = (int)c.charAt(0);
//				sbf.append(",").append(val);
//			}
//			System.out.println(sbf.toString());
//			value = sbf.toString();
			//转换成字符-end
			
			this.responseOK(ctx, type);
			MySQLDBService.getInstance().saveLog(code, type, value);

		} catch (Exception e) {
			logger.error("处理data异常", e);
		}

		// $PowData,1功放模块数量,2功放下标,3功放功率,4反射功率,5
		// 50V电压,6功放1电流值,7功放1温度值,8功放2电流值,9功放2温度值*校验值
		// String[] vals = value.split(",");
		// if(vals.length!=9){
		// logger.debug("PowData数据个数不对");
		// this.responseERROR(ctx, type);
		// return false;
		// }
		//
		// try {
		// MySQLDBService.getInstance().savePowDataLog(code, vals);
		// logger.debug("保存PowData成功");
		// this.responseOK(ctx, type);
		// return true;
		//
		// } catch (Exception e) {
		// logger.error("保存PowData错误", e);
		// this.responseERROR(ctx, type);
		// return false;
		// }

	}

	private void processGetetHeartFromWeb(ChannelHandlerContext ctx, String msg){
		String strs[] = msg.split(",");
		String code = strs[1];
		Heart heart = HeartUtil.getHeart(code);
		if(heart==null){
			ctx.channel().writeAndFlush("null");
		}else{
			ctx.channel().writeAndFlush(heart.getStatus()+","+heart.getTime().getTime());
		}
	}

	private void processQueryFromFromWeb(ChannelHandlerContext ctx, String msg) {
		String strs[] = msg.split(",");
		String code = strs[1];
		Channel channel = ChannelUtil.getChannel(code);
		if (channel == null || !channel.isActive()) {
			logger.debug("无法连接设备:" + code);
			ctx.channel().writeAndFlush("noconnect!");
			return;
		} else {
			ctx.channel().writeAndFlush("ok!");
			//TODO
			channel.writeAndFlush("$"+MsgType.Query+",?*x!");
		}

	}

	/**
	 * @param ctx
	 * @param code
	 * @param type
	 * @param value
	 */
	private void processSetAllFromWeb(ChannelHandlerContext ctx, String msg) {
		String strs[] = msg.split(",");
		String code = strs[1];
		
		Channel channel = ChannelUtil.getChannel(code);
		if (channel == null || !channel.isActive()) {
			logger.debug("无法连接设备:" + code);
			ctx.channel().writeAndFlush("noconnect!");
			return;
		} else {
			ctx.channel().writeAndFlush("ok!");
			// TODO
			//转换字符成数字-begin
			String value = msg.replace(MsgType.SetAllFromWeb+",", "").replace(code+",", "");
			StringBuffer sbf = new StringBuffer();
			String vals[] = value.split(",");
			for (String c : vals) {
//				int val = Integer.parseInt(c);
//				sbf.append(",").append((char)val);
				sbf.append(",").append(c);
			}
			value = sbf.toString();
			//转换字符成数字-end
			
			String sendMsg = "$"+MsgType.SetAll+value+"*x!";
			channel.writeAndFlush(sendMsg);
		}

	}

	private void responseERROR(ChannelHandlerContext ctx, String type) {
		this.write(ctx, null, type, "ERROR");
	}

	private void responseOK(ChannelHandlerContext ctx, String type) {
		this.write(ctx, null, type, "OK");
	}

	private void responseLoginERROR(ChannelHandlerContext ctx, String code) {
		this.write(ctx, code, MsgType.login, "ERROR");
	}

	private void responseLoginOK(ChannelHandlerContext ctx, String code) {
		this.write(ctx, code, MsgType.login, "OK");
	}

	private void write(ChannelHandlerContext ctx, String code, String type,
			String value) {
		// $login,设备序列号,ERROR*效验值<CR><LF>
		// 成功:$ SetPowTemp,OK*校验值<CR><LF>
		String str = type;
		if (code != null) {
			str += "," + code;
		}
		str += "," + value;
		String xor = StringUtil.toXor(str);

		str = "$" + str + "*" + xor + "!";

		ctx.channel().writeAndFlush(str);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.warn("Unexpected exception from downstream.", cause);
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.err.println("  reg:" + ctx.channel().hashCode());
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.err.println("unreg:" + ctx.channel().hashCode());
		super.channelUnregistered(ctx);
		ChannelUtil.removeChannel(ctx.channel().hashCode());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("  act:" + ctx.channel().hashCode());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("inact:" + ctx.channel().hashCode());
		super.channelInactive(ctx);
	}

}
