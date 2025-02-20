package com.example.webapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.webapp.entity.ToDo;
import com.example.webapp.exception.WebappException;
import com.example.webapp.form.ToDoForm;
import com.example.webapp.helper.ToDoHelper;
import com.example.webapp.service.ToDoService;

import lombok.RequiredArgsConstructor;

/**
 * ToDo管理のコントローラー
 */
@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class ToDoController {

	private final ToDoService toDoService;

	/**
	 * ToDo一覧を表示
	 * ユーザー名で所属するToDoを取得し、modelに詰めて一覧画面を返す
	 * @param model ToDoリスト(未完了：todosUnfinished、完了：todosFinished)
	 * @return ToDo一覧画面
	 */
	@GetMapping
	public String list(Model model) {
		// ログイン中のユーザー名を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		// modelにToDoをそれぞれ格納
		model.addAttribute("todosUnfinished", toDoService.findAllToDo(username, false));
		model.addAttribute("todosFinished", toDoService.findAllToDo(username, true));
		return "todo/list";
	}

	/**
	 * ToDo詳細の表示
	 * @param id 対象のToDoID
	 * @param model ToDoのentityを格納
	 * @param attributes フラッシュメッセージ
	 * @return ToDo詳細画面(またはリダイレクト)
	 */
	@GetMapping("/{id}")
	public String detail(@PathVariable Integer id, Model model,
			RedirectAttributes attributes) {
		try {
			//IDからToDo情報を取得
			ToDo toDo = toDoService.findByIdToDo(id);

			// 対象データがある場合はモデルに格納し詳細画面へ
			model.addAttribute("todo", toDo);
			return "todo/detail";
		} catch (WebappException e) {
			// 対象データ無し
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/todos";
		}
	}

	// === 登録・更新処理追加 ===
	/**
	 * ToDoの新規登録画面の表示
	 * @param form ToDoFormのentity
	 * @return 新規登録画面
	 */
	@GetMapping("/form")
	public String newToDo(@ModelAttribute ToDoForm form) {
		// 新規登録画面フラグの設定
		form.setIsNew(true);
		return "todo/form";
	}

	/**
	 * ToDoの新規登録実行
	 * バリデーションOKの場合、ToDoForm→ToDoに変換してDB登録し一覧画面へリダイレクト
	 * 					  NGの場合、ToDo登録画面へ
	 * @param form ToDoFormのentity
	 * @param bindingResult バリデーション結果
	 * @param attributes フラッシュメッセージ
	 * @return ToDo一覧画面またはToDo登録画面
	 */
	@PostMapping("/save")
	public String create(@Validated ToDoForm form, BindingResult bindingResult, RedirectAttributes attributes) {
		// ログイン中のユーザー名を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		form.setUsername(username);

		// バリデーション：NG
		if (bindingResult.hasErrors()) {
			form.setIsNew(true);
			return "todo/form";
		}

		// 登録実行
		String resultMessage = toDoService.insertToDo(form);

		// フラッシュメッセージを設定して一覧画面へリダイレクト
		attributes.addFlashAttribute("message", resultMessage);
		return "redirect:/todos";
	}

	/**
	 * ToDo編集画面の表示
	 * @param id ToDoID
	 * @param model ToDoForm
	 * @param attributes フラッシュメッセージ
	 * @return ToDo編集画面またはToDo一覧画面
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model,
			RedirectAttributes attributes) {
		try {
			// IDからToDoを取得
			ToDo target = toDoService.findByIdToDo(id);

			// ToDo→ToDoForm変換
			ToDoForm form = ToDoHelper.convertToDoForm(target);
			model.addAttribute("toDoForm", form);
			return "todo/form";
		} catch (WebappException e) {
			// 対象データがない
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/todos";
		}
	}

	/**
	 * ToDoのアップデート
	 * @param form ToDoForm
	 * @param bindingResult バリデーション結果
	 * @param attributes フラッシュメッセージ
	 * @return ToDo一覧画面またはToDo編集画面
	 */
	@PostMapping("/update")
	public String update(@Validated ToDoForm form, BindingResult bindingResult, RedirectAttributes attributes) {
		// バリデーションNGの場合、更新フラグを設定して更新画面へ
		if (bindingResult.hasErrors()) {
			form.setIsNew(false);
			return "todo/form";
		}

		// 更新処理
		try {
			String resultMessage = toDoService.updateToDo(form);
			attributes.addFlashAttribute("message", resultMessage);
			return "redirect:/todos";
		} catch (WebappException e) {
			// 更新対象が存在しない
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/todos";
		}
	}

	/**
	 * ToDoの処理ステータスを変更
	 * 完了の場合、未完了に変更
	 * 未完了の場合、完了に変更
	 * @param todo ToDo
	 * @param bindingResult バリデーション結果
	 * @param attributes フラッシュメッセージ
	 * @return ToDo一覧画面
	 */
	@PostMapping("/update/status")
	public String updateStatus(@Validated ToDo todo, BindingResult bindingResult, RedirectAttributes attributes) {
		// バリデーションNGの場合、一覧画面へリダイレクト
		if (bindingResult.hasErrors()) {
			return "redirect:/todos";
		}

		// 更新処理
		try {
			String resultMessage = toDoService.updateStatus(todo);
			attributes.addFlashAttribute("message", resultMessage);
			return "redirect:/todos";
		} catch (WebappException e) {
			// 対象が存在しない
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/todos";
		}
	}

	/**
	 * ToDoの削除
	 * @param id 削除対象のToDoID
	 * @param attributes フラッシュメッセージ
	 * @return ToDo一覧画面
	 */
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes attributes) {
		try {
			// 削除処理
			String resultMessage = toDoService.deleteToDo(id);
			// フラッシュメッセージ
			attributes.addFlashAttribute("message", resultMessage);
			return "redirect:/todos";
		} catch (WebappException e) {
			// 対象が存在しない
			attributes.addFlashAttribute("errorMessage", e.getMessage());
			return "redirect:/todos";

		}
	}
}