package com.example.webapp.controller;

import java.util.List;

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
import com.example.webapp.service.impl.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {
	private final AdminServiceImpl service;

	@GetMapping("/userlist")
	public String showUserList(Model model, RedirectAttributes attributes) {
		// ユーザーリストを取得
		List<Authentication> userList = service.getUserList();
		if (userList == null) {
			// ユーザーがいない場合リダイレクト(ありえない)
			attributes.addFlashAttribute("errorMessage", "ユーザーが存在しません");
			// リダイレクト
			return "redirect:/";
		}
		model.addAttribute("userlist", userList);
		return "admin/userlist";
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

		// adminユーザーではないか確認
		if (service.isNotDeleteUser(username)) {
			// 削除してはいけないユーザーの場合リダイレクト
			attributes.addFlashAttribute("errorMessage", "ユーザー名「admin」または操作中のユーザーのため削除できません。");
			return "redirect:/admin/userlist";
		}

		// 削除実行
		service.deleteUser(username);
		attributes.addFlashAttribute("message", "ユーザー：" + username + " 削除しました");

		return "redirect:/admin/userlist";
	}

	@GetMapping("/userRegistForm")
	public String registUserForm(Model model, RedirectAttributes attributes) {
		// ユーザー数を取得
		int userLength = service.getUserList().size();

		// ユーザー数が50を越える場合、ユーザー追加不可
		if (userLength >= 50) {
			attributes.addFlashAttribute("errorMessage", "ユーザー数が上限に達しているため追加できません。");
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

		// ユーザー重複チェック
		if (service.isRegistUser(authentication.getUsername())) {
			attributes.addFlashAttribute("errorMessage", "既に使われているユーザー名です。");
			return "redirect:/admin/userlist";
		}

		// ユーザー登録
		service.registUser(authentication);
		attributes.addFlashAttribute("message", "ユーザー：" + authentication.getUsername() + " を登録しました。");

		return "redirect:/admin/userlist";
	}

}
