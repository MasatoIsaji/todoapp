package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webapp.entity.Authentication;
import com.example.webapp.exception.WebappException;
import com.example.webapp.service.impl.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/createUserbeforeLogin")
public class CreateUserBeforeLoginController {
	private final AdminServiceImpl service;

	@PostMapping("/registUser")
	public String registUser(@Validated Authentication authentication, BindingResult bindingResult,
			Model model, RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", authentication);
			// バリデーションエラーの場合
			return "createUserbeforeLogin/createUser";
		}
		try {
			// ユーザー重複チェック
			service.isRegistUser(authentication.getUsername());
		} catch (WebappException e) {
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/login/createUser";
		}

		// ユーザー数上限(50ユーザーまで)チェック
		// ユーザー数を取得
		int userLength = service.getUserList().size();

		// ユーザー数が50を越える場合、ユーザー追加不可
		if (userLength >= 50) {
			attributes.addFlashAttribute("errorMessage", "ユーザー数が上限に達しているため追加できません。");
			return "redirect:/login/createUser";
		}

		// ユーザー登録
		service.registUser(authentication);
		attributes.addFlashAttribute("message", authentication.getUsername() + " を登録しました。");

		return "redirect:/login";
	}

}
