package com.example.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webapp.entity.Authentication;
import com.example.webapp.exception.WebappException;
import com.example.webapp.service.impl.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {
	private final AdminServiceImpl service;

	@GetMapping("/userlist")
	public String showUserList(Model model, RedirectAttributes attributes) {
		try {
			// ユーザーリストを取得し、modelに詰めて返却
			model.addAttribute("userlist", service.getUserList());
			return "admin/userlist";

		} catch (WebappException e) {
			// ユーザーがいない場合リダイレクト(ありえない)
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/";
		}
	}

	/**
	 * ユーザーの削除
	 * 削除対象外：ユーザー名「admin」またはカレントユーザー
	 * @param username 削除対象ユーザー名
	 * @param attributes フラッシュメッセージ
	 * @return リダイレクトパス
	 */
	@GetMapping("/delete/{username}")
	public String deleteUser(@PathVariable String username, RedirectAttributes attributes) {
		if (username == null) {
			// usernameを取得できなかった場合リダイレクト
			attributes.addFlashAttribute("errorMessage", "ユーザー名が取得できません。");
			return "redirect:/admin/userlist";
		}
		try {
			// 削除しても問題ないかチェック(削除対象外の場合例外発生)
			service.isNotDeleteUser(username);

			// 削除実行
			String resultMessage = service.deleteUser(username);
			attributes.addFlashAttribute("message", resultMessage);
			return "redirect:/admin/userlist";
		} catch (WebappException e) {
			// 削除対象が存在しない場合リダイレクト
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/admin/userlist";
		}
	}

	@GetMapping("/userRegistForm")
	public String registUserForm(Model model, RedirectAttributes attributes) {
		// ユーザー数を取得
		int userLength = service.getUserList().size();

		// ユーザー数が50を越える場合、ユーザー追加不可
		if (userLength >= 50) {
			attributes.addFlashAttribute("errorMessage", "ユーザー数が上限に達しているため追加できません");
			return "redirect:/admin/userlist";
		}
		Authentication auth = new Authentication();
		model.addAttribute("authentication", auth);

		return "admin/registUserForm";
	}

	@PostMapping("/registUser")
	public String registUser(@Validated Authentication authentication, BindingResult bindingResult,
			Model model, RedirectAttributes attributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", authentication);
			// バリデーションエラーの場合
			return "admin/registUserForm";
		}
		try {
			// ユーザー重複チェック
			service.isRegistUser(authentication.getUsername());
		} catch (WebappException e) {
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/admin/userlist";
		}
		// ユーザー登録
		String resultMessage = service.registUser(authentication);
		attributes.addFlashAttribute("message", resultMessage);
		return "redirect:/admin/userlist";
	}
}
