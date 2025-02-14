package com.example.webapp.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.webapp.entity.Authentication;

@Mapper
public interface AuthenticationMapper {

	/**
	* ユーザー名でログイン情報を取得します。
	*/
	Authentication selectByUsername(String username);

	/**
	 * ユーザー一覧を取得します。
	 */
	List<Authentication> selectUserList();

	/**
	 * ユーザーを登録します。
	 */
	void insertUser(Authentication auth);

	/**
	 * ユーザーを削除します。
	 */
	void deleteUser(@Param("username") String username);

}