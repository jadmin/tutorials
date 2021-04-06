/*
 * @(#)GoogleAuthTest.java	2021-3-8
 *
 * Copyright (c) 2021. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.google.authenticator;

import org.junit.Test;

public class GoogleAuthTest {  
    
    @Test  
    public void genSecretTest() {  
        String secret = GoogleAuthenticator.generateSecretKey();  
        String url = GoogleAuthenticator.getQRBarcodeURL("testuser", "testhost", secret);  
        System.out.println("Please register " + url);  
        System.out.println("Secret key is " + secret);  
    }  
      
    // Change this to the saved secret from the running the above test.   
    static String savedSecret = "SFTVVYJCMCMSBQRN";  
      
    @Test  
    public void authTest() {  
        // enter the code shown on device. Edit this and run it fast before the code expires!  
        long code = 248804;  
        long t = System.currentTimeMillis();  
        GoogleAuthenticator ga = new GoogleAuthenticator();  
        ga.setWindowSize(3); // 3 * 30 秒内有效 
        boolean r = ga.check_code(savedSecret, code, t);  
        System.out.println("Check code = " + r);  
    }  
}  
