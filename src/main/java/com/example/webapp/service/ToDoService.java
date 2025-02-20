package com.example.webapp.service;

import java.util.List;

import com.example.webapp.entity.ToDo;
import com.example.webapp.form.ToDoForm;

/**
 * ToDoサービスインターフェース
 */
public interface ToDoService {

	// ユーザーに所属するToDoを全てリストで取得
	List<ToDo> findAllToDo(String username, boolean status);

	//  指定されたIDのToDoを取得
	ToDo findByIdToDo(Integer id);

	//  ToDoを新規登録
	String insertToDo(ToDoForm form);

	// ToDoを更新
	String updateToDo(ToDoForm form);

	// ToDoの処理ステータスを更新しメッセージを返す
	String updateStatus(ToDo toDo);

	// 指定されたIDのToDoを削除
	String deleteToDo(Integer id);
}