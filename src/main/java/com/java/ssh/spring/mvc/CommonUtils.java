package com.java.ssh.spring.mvc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {
	
	public static String getClientIP(HttpServletRequest request) {

		if (request == null)
			return null;
		String s = request.getHeader("X-Forwarded-For");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("Proxy-Client-IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("WL-Proxy-Client-IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("HTTP_CLIENT_IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getRemoteAddr();
		if ("127.0.0.1".equals(s) || "0:0:0:0:0:0:0:1".equals(s))
			try {
				s = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException unknownhostexception) {
				// ignore
			}
		return s;
	}

}
