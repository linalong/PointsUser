package com.heizi.mylibrary.model;

/**
 * 错误信息
 * 
 * @author Lin
 * 
 */
public class ErrorModel {
	/** 错误代码 **/
	private int errorCode;
	/** 错误信息 **/
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return errorCode;
	}

	public void setCode(int code) {
		this.errorCode = code;
	}

}
