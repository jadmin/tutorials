/*
 * @(#)IntegerInJvm.java	2017-03-25
 *
 * Copyright (c) 2017. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.jvm.memory;

/**
 * IntegerInJvm
 * 
 * 负整形数在Jvm中的表示
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: IntegerInJvm.java 2017-03-25 2017-03-25 11:08:26 Exp $
 */
public class IntegerInJvm {

	public static void main(String[] args) {
        int a = -10;
        for (int i = 0; i < 32; i++){
            // 0x80000000?
            // 0100,0000,0000,0000,0000,0000,0000,0000
            // ?????????0
            int t = (a & 0x80000000 >>> i) >>> (31 - i);
            System.out.print(t);
        }
        System.out.println();
        // ???
        System.out.println(Integer.toBinaryString(a));
    }
}
