/*
 * @(#)GoogleAuthenticatorUtils.java	2021-3-8
 *
 * Copyright (c) 2021. All Rights Reserved.
 *
 */

package org.javaclub.opensource.demos.google.authenticator;

import org.junit.Test;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * GoogleAuthenticatorUtils
 *
 * @author <a href="mailto:gerald.chen.hz@gmail.com">Gerald Chen</a>
 * @version $Id: GoogleAuthenticatorUtils.java 2021-3-8 17:32:42 Exp $
 */
public class GoogleAuthenticatorUtils {
	
	@Test
	public void generateQrcode() {
        com.warrenstrange.googleauth.GoogleAuthenticator gAuth  = new com.warrenstrange.googleauth.GoogleAuthenticator();
        final GoogleAuthenticatorKey gakey = gAuth .createCredentials();
        
        String totpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthURL("XINC-SSO-DEV", "chenzq", gakey);
        System.out.println("user=chenzq, totpAuthURL=" + totpAuthURL);
        System.out.println("secretKey=" + gakey.getKey());
    }
	
	@Test
	public void testValid() {
		String secretKey = "ASL5L472JGEYA5G3MHXHROJZ2YDWVEVV";
		
		int code = 421034;
		boolean verifyR = verify(secretKey, code);
		System.out.println("verifyResult => " + verifyR);
	}

	public static String generateSecretKey() {
        com.warrenstrange.googleauth.GoogleAuthenticator gAuth  = new com.warrenstrange.googleauth.GoogleAuthenticator();
        final GoogleAuthenticatorKey gakey = gAuth .createCredentials();
        String key = gakey.getKey();
        return key;
    }
	
	public static boolean verify(String key, int password) {
		com.warrenstrange.googleauth.GoogleAuthenticator gAuth = new com.warrenstrange.googleauth.GoogleAuthenticator();
        boolean isPattern = gAuth.authorize(key,password);
        return isPattern;
    }
 
 
    /**
     * 获得TOTF算法生成的验证码,根据时间产生
     * @param secretKey   安全码
     * @return
     */
    public static int getVercodeByTime(String secretKey) {
    	com.warrenstrange.googleauth.GoogleAuthenticator gAuth = new com.warrenstrange.googleauth.GoogleAuthenticator();
        int code = gAuth.getTotpPassword(secretKey);
        return code;
    }

}
