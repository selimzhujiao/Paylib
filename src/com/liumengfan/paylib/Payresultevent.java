package com.liumengfan.paylib;


public class Payresultevent extends ObjectEvent{
	
	private int status;
	private String msg;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
	public static Payresultevent create(int status,String msg){
		Payresultevent orderEvent = new Payresultevent();
		orderEvent.setMsg(msg);
		orderEvent.setStatus(status);
		return orderEvent;
	}
	
	
	public static Payresultevent create(int status){
		
		return create(status,"");
	}
	
	
}
