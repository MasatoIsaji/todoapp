package com.example.webapp.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
* ハッシュ化した文字列を返すクラス
*/
public class PasswordGenerator {
	public static String generateHashedPassword(String rawPassword) {
		// 「BCrypt」のインスタンス化
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// パスワードをハッシュ化
		String encodedPassword = encoder.encode(rawPassword);

		return encodedPassword;
	}
}