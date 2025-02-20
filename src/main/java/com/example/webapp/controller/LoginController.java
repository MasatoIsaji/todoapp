package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.webapp.entity.Authentication;
import com.example.webapp.form.LoginForm;

/**
 * ログイン画面(認証前のアクセス画面)
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	/**
	 * ログイン画面の表示
	 * @param form 入力フォーム
	 * @return 入力画面
	 */
	@GetMapping
	public String showLogin(@ModelAttribute LoginForm form) {
		return "login";
	}

	/**
	 * ログイン画面→ユーザー登録画面
	 * ログイン前のユーザー登録は全て一般(USER)とする
	 * @param authentication ユーザー登録フォーム
	 * @return ユーザー登録画面
	 */
	@GetMapping("/createUser")
	public String showCreateUserForm(@ModelAttribute Authentication authentication) {
		authentication.setAuthority("USER");
		return "createUserbeforeLogin/createUser";
	}

}