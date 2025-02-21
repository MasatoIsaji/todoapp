package com.example.webapp.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.webapp.entity.Authentication;
import com.example.webapp.exception.WebappException;
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
		List<Authentication> resultList = authenticationMapper.selectUserList();
		if (resultList == null) {
			throw new WebappException("ユーザーが存在しません");
		}
		return resultList;
	}

	@Override
	public String registUser(Authentication auth) {
		// パスワードをハッシュ化
		String hashedPassword = PasswordGenerator.generateHashedPassword(auth.getPassword());
		auth.setPassword(hashedPassword);

		authenticationMapper.insertUser(auth);
		return "ユーザー：" + auth.getUsername() + " を登録しました。";
	}

	@Override
	public String deleteUser(String username) {
		// 削除対象が存在するかチェック
		Authentication auth = authenticationMapper.selectByUsername(username);
		if (auth == null) {
			throw new WebappException("削除対象が存在しません");
		}

		authenticationMapper.deleteUser(username);
		return "ユーザー：" + username + " 削除しました";
	}

	@Override
	public void isNotDeleteUser(String username) {
		// ユーザー「admin」の場合NG
		if (username.equals("admin")) {
			throw new WebappException("adminは削除できません");
		}
		// カレントユーザー名の取得
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		String currentUsername = authentication.getName();

		// カレントユーザーの場合NG
		if (username.equals(currentUsername)) {
			throw new WebappException("操作中のユーザーは削除できません");
		}
	}

	@Override
	public void isRegistUser(String username) {
		// 登録ユーザーと同一のユーザーがいないか確認
		Authentication auth = authenticationMapper.selectByUsername(username);
		// 重複無し
		if (auth == null) {
			throw new WebappException("既に使われているユーザー名です");
		}
	}
}
