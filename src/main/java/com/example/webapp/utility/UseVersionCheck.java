package com.example.webapp.utility;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class UseVersionCheck {
	public static void main(String[] args) {
		// Spring Frameworkのバージョン
		String springVersion = SpringVersion.getVersion();
		System.out.println("Spring Framework : " + springVersion);

		// Spring Bootのバージョン
		String bootVersion = SpringBootVersion.getVersion();
		System.out.println("Spring Boot : " + bootVersion);

		// Spring Securityのバージョン
		String securityVersion = SpringSecurityCoreVersion.getVersion();
		System.out.println("Spring Security : " + securityVersion);

		// MyBatisのバージョン
		String myBatisVersion = org.apache.ibatis.io.Resources.class.getPackage().getImplementationVersion();
		System.out.println("MyBatis : " + myBatisVersion);

	}
}