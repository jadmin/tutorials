/*
 * @(#)RegexTest.java	2011-9-16
 *
 * Copyright (c) 2011. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.databene.feed4junit;

import javax.validation.constraints.Pattern;

import org.databene.feed4junit.Feeder;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * RegexTest
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: RegexTest.java 2011-9-16 下午05:02:45 Exp $
 */
@RunWith(Feeder.class)
public class RegexTest {

    @Test
    public void testSmoke(@Pattern(regexp = "[A-Z][a-z]{3,8}") String name) {
        System.out.println("name:" + name);
    }
   
} 