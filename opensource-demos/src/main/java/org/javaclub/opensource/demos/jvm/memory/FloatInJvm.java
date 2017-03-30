/*
 * @(#)FloatInJvm.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.memory;

/**
 * FloatInJvm
 * 
 * 输出浮点数在虚拟机的实际表示
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: FloatInJvm.java 2017-03-25 2017-03-25 11:09:57 Exp $
 */
public class FloatInJvm {

	public static void main(String[] args) {
        float a = -5;
        // 输出-5的补码，即虚拟机内部实际表示
        System.out.println(Integer.toBinaryString(Float.floatToRawIntBits(a)));
    }
}
