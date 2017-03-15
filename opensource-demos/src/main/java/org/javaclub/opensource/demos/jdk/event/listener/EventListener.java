/*
 * @(#)EventListener.java	2012-2-16
 *
 * Copyright (c) 2012. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jdk.event.listener;

import org.javaclub.opensource.demos.jdk.event.Event;

/**
 * EventListener
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: EventListener.java 2012-2-16 13:54:52 Exp $
 */
public interface EventListener<E extends Event> extends java.util.EventListener {

	void onEvent(E event);
}
