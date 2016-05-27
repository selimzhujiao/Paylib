/**
 * 
 */
package com.liumengfan.paylib;

import com.fph.appnavigationlib.eventbus.BusProvider;

/**
 *
 * com.fph.client.hui.event.bean.ObjectEvent.java
 * 
 * Created by wang on 20162016年5月24日下午3:18:29 
 * 
 * Tips:
 */
public class ObjectEvent {

	
	private String className;

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 发送事件通知
	 *	postEvent(Constants.EventType.TAG_STORY, mouseMam.birth());
	 * @param tag 标记
	 
	 */
	public void postEvent(String tag){
		try {
			BusProvider.getInstance().post(tag, this);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
