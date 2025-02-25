package com.example.webapp.form;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoForm {
	/** ToDoID */
	private Integer id;
	/** ToDoの持ち主 */
	private String username;
	/** 処理ステータス */
	@NotNull(message = "不正なデータです。")
	private boolean status;
	/** ToDo */
	@NotBlank(message = "ToDoは必須です。")
	@Size(min = 1, max = 26, message = "ToDoは{min}〜{max}文字以内で入力してください。")
	private String todo;
	/** ToDo詳細 */
	@Size(min = 1, max = 100, message = "詳細は{min}〜{max}文字以内で入力してください。")
	private String detail;
	/** 優先順位 */
	@Range(min = 0, max = 3, message = "優先順位を選択してください。")
	private Integer priority;
	/** 新規判定 */
	private Boolean isNew;
}