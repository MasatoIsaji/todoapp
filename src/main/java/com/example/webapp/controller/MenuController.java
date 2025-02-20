package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * イニシャルアクセス時に表示する画面
 * SpringSecurityにより認証が通っていない場合は/loginに遷移する
 */
@Controller
@RequestMapping("/")
public class MenuController {

	/**
	 * メニュー画面を表示する
	 */
	@GetMapping
	public String showMenu() {
		return "menu";
	}
}
