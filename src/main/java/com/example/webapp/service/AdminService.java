package com.example.webapp.service;

import java.util.List;

import com.example.webapp.entity.Authentication;

public interface AdminService {

	// ユーザー一覧の取得
	List<Authentication> getUserList();

	// ユーザーの登録
	String registUser(Authentication auth);

	// ユーザーの削除
	String deleteUser(String username);

	// 削除対象外(adminまたはカレントユーザー)か確認
	void isNotDeleteUser(String username);

	// 登録可能か
	void isRegistUser(String username);
}
