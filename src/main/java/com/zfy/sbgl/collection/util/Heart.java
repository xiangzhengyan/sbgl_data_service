package com.zfy.sbgl.collection.util;

import java.sql.Timestamp;


public class Heart {

	protected Long id;


	protected String code;
	

	private Timestamp time;
	

	private int status;

	
	

	public Heart(Timestamp time, int status) {
		super();
		this.time = time;
		this.status = status;
	}

	/**
	 * 
	 */
	public Heart() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
