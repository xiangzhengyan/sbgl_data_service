/**
 * 版权所有 2013 成都子非鱼软件有限公司 保留所有权利
 * 1 项目签约客户只拥有对项目业务代码的所有权，以及在本项目范围内使用平台框架
 * 2 平台框架及相关代码属子非鱼软件有限公司所有，未经授权不得扩散、二次开发及用于其它项目
 */
package com.zfy.sbgl.common.mq;

/**
 * 数据采集器上传数据，采集服务保存数据到数据库后，发送此消息
 * @author xiangzy
 * @date 2015-1-5
 * 
 */
public class DataCollectedMessage extends BaseMesssage {

	/**
	 * @param rowID
	 */
	public DataCollectedMessage(String rowID) {
		super(MessageType.DATA_COLLECTED);
		this.rowID = rowID;
	}

	private String rowID;

	public String getRowID() {
		return rowID;
	}

	public void setRowID(String rowID) {
		this.rowID = rowID;
	}

}
