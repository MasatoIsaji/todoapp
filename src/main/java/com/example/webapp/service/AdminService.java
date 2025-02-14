package com.example.webapp.service;

import java.util.List;

import com.example.webapp.entity.Authentication;

public interface AdminService {

	// ユーザー一覧の取得
	List<Authentication> getUserList();

	// ユーザーの登録
	void registUser(Authentication auth);

	// ユーザーの削除
	void deleteUser(String username);

	// 削除対象外(adminまたはカレントユーザー)か確認
	boolean isNotDeleteUser(String username);

	// 登録可能か
	boolean isRegistUser(String username);

	// パスワードをハッシュ化
	String createHashedPassword(String warPassword);

}
