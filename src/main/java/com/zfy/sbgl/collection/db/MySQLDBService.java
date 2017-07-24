/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfy.sbgl.collection.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zfy.sbgl.collection.cfg.Config;
import com.zfy.sbgl.collection.util.Heart;
import com.zfy.sbgl.collection.util.TimeUtil;

/**
 * @author xiangzy
 * @date 2014-12-26
 * 
 */
public class MySQLDBService

{
	private static long createTime = System.currentTimeMillis();
	private static MySQLDBService dbService;
	private static final Logger logger = Logger.getLogger(MySQLDBService.class);
	private Connection conn;

	private MySQLDBService() {
		try {
			Class.forName(Config.dbDriver);
		} catch (Exception e) {
			logger.error("连接数据库失败", e);
		}
	}

	private Connection getConn() {
		try {
			if (conn == null
					||conn.isClosed()|| System.currentTimeMillis() - createTime > 7600000) {
				createTime = System.currentTimeMillis();
				conn = DriverManager.getConnection(Config.dbUrl,
						Config.dbUsername, Config.dbPassword);
			}
		} catch (SQLException e) {
			logger.error("创建conn失败", e);
		}

		return conn;
	}

	//

	public static MySQLDBService getInstance() {
		if (dbService == null) {
			dbService = new MySQLDBService();
		}
		return dbService;

	}

	public void saveLog(String code, String type, String value)
			throws Exception {
		String sql = "insert into sb_data_log(code,time,type,value) values(?,?,?,?)";
		PreparedStatement pstmt = getConn().prepareStatement(sql);
		pstmt.setString(1, code);
		pstmt.setString(2, TimeUtil.getCurrTimeStr());
		pstmt.setString(3, type);
		pstmt.setString(4, value);
		pstmt.executeUpdate();
		pstmt.close();

	}

	public void saveLoginLog(String code, int type) throws Exception {
		String sql = "insert into sb_login_log(code,time,type) values(?,?,?)";
		PreparedStatement pstmt = getConn().prepareStatement(sql);
		pstmt.setString(1, code);
		pstmt.setString(2, TimeUtil.getCurrTimeStr());
		pstmt.setInt(3, type);
		pstmt.executeUpdate();
		pstmt.close();
	}

	public void savePreDataLog(String code, String[] vals) throws Exception {
		String tableName = "sb_predata_log";
		StringBuffer sbf = new StringBuffer();
		sbf.append("insert into ").append(tableName);
		sbf.append("(");
		sbf.append("code,time");
		// 10个数据字段
		sbf.append(",total_out_power,total_ref_power,agc_vol,pre_in_power,pre_out_level,sys_temp,pad_value,total_ref_limit,total_in_lower_limit,total_in_upper_limit");
		sbf.append(") values(");
		sbf.append("'" + code + "'");
		sbf.append(",'" + TimeUtil.getCurrTimeStr() + "'");
		for (int i = 0; i < vals.length; i++) {
			if ("null".equals(vals[i])) {
				sbf.append(",null");
			} else {
				sbf.append(",'" + vals[i] + "'");
			}
		}

		sbf.append(") ");

		PreparedStatement pstmt = getConn().prepareStatement(sbf.toString());
		pstmt.executeUpdate();
		pstmt.close();
	}

	/**
	 * @param code
	 * @param vals
	 */
	public void savePowDataLog(String code, String[] vals) throws Exception {
		String tableName = "sb_powdata_log";
		StringBuffer sbf = new StringBuffer();
		sbf.append("insert into ").append(tableName);
		sbf.append("(");
		sbf.append("code,time");
		// 10个数据字段
		sbf.append(",amp_count,amp_num,amp_power,ref_power,vol50,amp_current_1,amp_temp_1,amp_current_2,amp_temp_2");
		sbf.append(") values(");
		sbf.append("'" + code + "'");
		sbf.append(",'" + TimeUtil.getCurrTimeStr() + "'");
		for (int i = 0; i < vals.length; i++) {
			if ("null".equals(vals[i])) {
				sbf.append(",null");
			} else {
				sbf.append(",'" + vals[i] + "'");
			}
		}

		sbf.append(") ");

		PreparedStatement pstmt = getConn().prepareStatement(sbf.toString());
		pstmt.executeUpdate();
		pstmt.close();
	}

	/**
	 * @param code
	 * @param vals
	 */
	public void saveSetPowDataLog(String code, String[] vals) throws Exception {
		String tableName = "sb_setpowdata_log";
		StringBuffer sbf = new StringBuffer();
		sbf.append("insert into ").append(tableName);
		sbf.append("(");
		sbf.append("code,time");
		// 10个数据字段
		sbf.append(",fan_temp,warn_temp,warn_curr,warn_ref");
		sbf.append(") values(");
		sbf.append("'" + code + "'");
		sbf.append(",'" + TimeUtil.getCurrTimeStr() + "'");
		for (int i = 0; i < vals.length; i++) {
			if ("null".equals(vals[i])) {
				sbf.append(",null");
			} else {
				sbf.append(",'" + vals[i] + "'");
			}
		}

		sbf.append(") ");

		PreparedStatement pstmt = getConn().prepareStatement(sbf.toString());
		pstmt.executeUpdate();
		pstmt.close();
	}

	public void saveHeart(Map<String, Heart> map) throws Exception {
		Statement stmt = getConn().createStatement();
		ResultSet rs = stmt
				.executeQuery("select code,time,status from sb_heart");
		Map<String, Heart> oldMap = new HashMap<String, Heart>();
		while (rs.next()) {
			Heart oldHeart = new Heart();
			oldHeart.setCode(rs.getString("code"));
			oldHeart.setTime(rs.getTimestamp("time"));
			oldHeart.setStatus(rs.getInt("status"));
			oldMap.put(oldHeart.getCode(), oldHeart);

		}
		stmt.close();

		stmt = getConn().createStatement();
		for (String code : map.keySet()) {
			Heart heart = map.get(code);
			Heart oldHeart = oldMap.get(code);
			if (oldHeart != null) {
				// update
				if (oldHeart.getStatus() != heart.getStatus()
						|| !TimeUtil.format(oldHeart.getTime()).equals(
								TimeUtil.format(heart.getTime()))) {
					stmt.addBatch("update sb_heart set time='"
							+ TimeUtil.format(heart.getTime()) + "',  status="
							+ heart.getStatus() + " where code='" + code + "'");
				}
			} else {
				// insert
				stmt.addBatch("insert sb_heart(code,time,status) values('"
						+ code + "','" + TimeUtil.format(heart.getTime())
						+ "'," + heart.getStatus() + ")");
			}
		}

		stmt.executeBatch();

	}

	public boolean login(String code, String pass) throws Exception {
		String sql = "select * from sb_sb where code=? and passowrd=?";
		PreparedStatement pstmt = getConn().prepareStatement(sql);
		pstmt.setString(1, code);
		pstmt.setString(2, pass);
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	}

}
