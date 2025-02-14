package com.example.webapp.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
	/** ユーザー名 */
	@Size(min = 1, max = 50, message = "ユーザー名は{min}〜{max}文字以内で入力してください。")
	private String username;
	/** パスワード */
	@Size(min = 1, max = 255, message = "パスワードは{min}〜{max}文字以内で入力してください。")
	private String password;
	/** 権限 */
	@NotEmpty(message = "権限はどちらかを必ず選択してください。")
	private String authority;
}