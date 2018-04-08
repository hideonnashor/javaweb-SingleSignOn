package pers.sivous.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;

import sun.misc.BASE64Encoder;

public class GetMd5Cookie {
	
	private GetMd5Cookie() {
	}
	
	public static Cookie createCookie(String username,int expirestime,String password) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("md5");
		BASE64Encoder encoder = new BASE64Encoder();
		//
		String value02 = password;
		byte result02[] = md.digest(value02.getBytes());
		String md5_02 = encoder.encode(result02);
		//
//		String value01 = String.valueOf(expirestime);
//		byte result01[] = md.digest(value01.getBytes());
//		String md5_01 = encoder.encode(result01);
		//
		Long expirestimeFormNow = System.currentTimeMillis()+expirestime*1000;
		
		String autologin = username+":"+expirestimeFormNow+":"+md5_02;
		
		Cookie cookie = new Cookie("autologin", autologin);
		cookie.setMaxAge(expirestime);
		cookie.setPath("/Filter");
		
		return cookie;
	}
	
}
