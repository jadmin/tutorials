/*
 * @(#)LoginTest.java	2011-9-16
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.databene.feed4junit;

import org.databene.benerator.anno.Source;
import org.databene.feed4junit.Feeder;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * LoginTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: LoginTest.java 2011-9-16 下午04:49:57 Exp $
 */
@RunWith(Feeder.class)
public class LoginTest {

	@Test
	@Source("login.csv")
    public void testLogin(String name, String password) {
        System.out.println("name:" + name + " password:" + password);
    }
}
