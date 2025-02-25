package com.example.webapp.helper;

import com.example.webapp.entity.ToDo;
import com.example.webapp.form.ToDoForm;

/**
 * ToDoとToDoFormの変換クラス
 */
public class ToDoHelper {
	/**
	 * ToDoへの変換
	 */
	public static ToDo convertToDo(ToDoForm form) {
		ToDo todo = new ToDo();
		todo.setId(form.getId());
		todo.setUsername(form.getUsername());
		todo.setStatus(false);
		todo.setTodo(form.getTodo());
		todo.setDetail(form.getDetail());
		todo.setPriority(form.getPriority());
		return todo;
	}

	/**
	 * ToDoFormへの変換
	 */
	public static ToDoForm convertToDoForm(ToDo todo) {
		ToDoForm form = new ToDoForm();
		form.setId(todo.getId());
		form.setUsername(todo.getUsername());
		form.setStatus(false);
		form.setTodo(todo.getTodo());
		form.setDetail(todo.getDetail());
		form.setPriority(todo.getPriority());
		// 更新画面設定
		form.setIsNew(false);
		return form;
	}
}