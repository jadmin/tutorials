/*
 * @(#)Event.java	2012-2-16
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.event;

import java.util.EventObject;
import java.util.UUID;

/**
 * Event
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: Event.java 2012-2-16 11:26:44 Exp $
 */
public abstract class Event extends EventObject {
	
	private static final long serialVersionUID = 1L;
	
	private final String eventId;// 事件ID，能唯一标识一次事件请求
	private final String messageId;// 消息ID
	
	public Event(String messageId, Object source) {
		super(source);
		this.eventId = UUID.randomUUID().toString();
		this.messageId = messageId;
	}

	public String getEventId() {
		return eventId;
	}

	public String getMessageId() {
		return messageId;
	}
	
}
