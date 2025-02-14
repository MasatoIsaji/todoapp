package com.example.webapp.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.webapp.entity.Authentication;
import com.example.webapp.repository.AuthenticationMapper;
import com.example.webapp.service.AdminService;
import com.example.webapp.utility.PasswordGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final AuthenticationMapper authenticationMapper;

	@Override
	public List<Authentication> getUserList() {
		return authenticationMapper.selectUserList();
	}

	@Override
	public void registUser(Authentication auth) {
		// パスワードをハッシュ化
		String hashedPassword = PasswordGenerator.generateHashedPassword(auth.getPassword());
		auth.setPassword(hashedPassword);

		authenticationMapper.insertUser(auth);
	}

	@Override
	public void deleteUser(String username) {
		authenticationMapper.deleteUser(username);
	}

	@Override
	public boolean isNotDeleteUser(String username) {
		// ユーザー「admin」の場合NG
		if (username.equals("admin")) {
			return true;
		}
		// カレントユーザー名の取得
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUsername = authentication.getName();

		// カレントユーザーの場合NG
		if (username.equals(currentUsername)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isRegistUser(String username) {
		// 登録ユーザーと同一のユーザーがいないか確認
		Authentication auth = authenticationMapper.selectByUsername(username);
		// 重複無し
		if (auth == null) {
			return false;
		}
		return true;
	}

	@Override
	public String createHashedPassword(String warPassword) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
