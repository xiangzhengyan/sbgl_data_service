/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfy.sbgl.collection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.zfy.sbgl.collection.cfg.Config;
import com.zfy.sbgl.collection.handler.DataCollectionHandler;
import com.zfy.sbgl.collection.util.ChannelUtil;
import com.zfy.sbgl.collection.util.HeartUtil;

/**
 * @author xiangzy
 * @date 2014-12-22
 * 
 */
public class Main {
	private static final Logger logger = Logger
			.getLogger(Main.class);

	/** 用于分配处理业务线程的线程组个数 */
	private static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; // 默认
	/** 业务出现线程大小 */
	private static final int BIZTHREADSIZE = 4;
	
	private static ServerBootstrap b ;
	private static EventLoopGroup bossGroup ;
	private static  EventLoopGroup workerGroup;

	public static void run() throws Exception {
		
		 b = new ServerBootstrap();
		
		bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
		workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);
		b.group(bossGroup, workerGroup);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024,true,false,delimiter()));
				pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast(new DataCollectionHandler());
			}
		});
		
		b.bind(new InetSocketAddress(Config.serverPort)).sync();
		
	}
	
    public static ByteBuf[] delimiter() {
        return new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[] {'!'})
//                0x0D,0x0A
        };
    }

	public static void main(String[] args) throws Exception {
		
		
		Properties props = System.getProperties();
		StringBuffer sbf = new StringBuffer();
		sbf.append("\n******************************************************");
		sbf.append("\n* 开始启动数据采集服务..." );
		sbf.append("\n* version:"+Config.version);
		sbf.append("\n* JC_ENV:"+Config.env);
		sbf.append("\n* host:" +InetAddress.getLocalHost()+"; port:"+Config.serverPort);
		sbf.append("\n* dbHost:"+Config.dbUrl+"; dbPort:"+Config.dbUrl);
		sbf.append("\n* mqHost:"+Config.mqHost+"; mqPort:"+Config.mqPort+"; mqUsername:"+Config.mqUsername+"; mqPassword:"+Config.mqPassword+"; mqVirtualHost:"+Config.mqVirtualHost);
		sbf.append("\n* osName:"+props.getProperty("os.name")+"; osArch:"+props.getProperty("os.arch")+"; osVersion:"+props.getProperty("os.version"));
		sbf.append("\n* userName:"+props.getProperty("user.name")+"; userHome:"+props.getProperty("user.home")+"; userDir:"+props.getProperty("user.dir"));
		sbf.append("\n******************************************************");
		logger.info(sbf.toString());
		
		Main.run();
		
		HeartUtil.startSaveTask();
		
		StringBuffer sbf2 = new StringBuffer();
		sbf2.append("\n******************************************************");
		sbf2.append("\n* 数据采集服务器已启动" );
		sbf2.append("\n* version:"+Config.version);
		sbf2.append("\n******************************************************");
		logger.info(sbf2.toString());
	}


}
