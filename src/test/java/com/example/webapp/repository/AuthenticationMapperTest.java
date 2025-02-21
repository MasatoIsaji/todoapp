package com.example.webapp.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import com.example.webapp.entity.Authentication;

/**
 * @MybatisTest 
 * テスト前にTransactionがかかり、テスト後にrollbackされる
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AuthenticationMapperTest {

	Authentication auth = new Authentication("testUser", "password", "USER");

	@Autowired
	private AuthenticationMapper authenticationMapper;

	/**
	 * 試験対象：selectByUsernameメソッド
	 * 試験内容：
	 * ・投入したユーザーが取得できること
	 * ・例外が発生しないこと
	 */
	@Test
	void selectByUsernameTest() {
		/**
		 * テストデータの投入
		 */
		authenticationMapper.insertUser(auth);

		/**
		 * 試験
		 */
		Authentication resultAuth = authenticationMapper.selectByUsername(auth.getUsername());
		assertThat(resultAuth).isNotNull();
		assertThat(resultAuth.getUsername()).isEqualTo(auth.getUsername());
	}

	/**
	 * 試験対象：selectUserListメソッド
	 * 試験内容：
	 * ・投入したユーザーが取得できること
	 *・投入した３ユーザー以上のリストが返ること
	 * ・例外が発生しないこと
	 */
	@Test
	void selectUserListTest() {
		/**
		 * テストデータの投入
		 */
		Authentication auth2 = new Authentication("testUser2", "password", "USER");
		Authentication auth3 = new Authentication("testUser3", "password", "USER");
		authenticationMapper.insertUser(auth);
		authenticationMapper.insertUser(auth2);
		authenticationMapper.insertUser(auth3);

		List<Authentication> authList = authenticationMapper.selectUserList();
		assertThat(authList).isNotEmpty();
		assertThat(authList.size() >= 3).isTrue();

	}

	/**
	 * 試験対象：insertUserメソッド
	 * 試験内容：
	 * ・投入したユーザーが取得できること
	 * ・例外が発生しないこと
	 */
	@Test
	void insertUserTest() {
		/**
		 * テストデータの投入
		 */
		authenticationMapper.insertUser(auth);

		/**
		 * 試験
		 */
		Authentication insertedAuth = authenticationMapper.selectByUsername(auth.getUsername());
		assertThat(insertedAuth).isNotNull();
		assertThat(insertedAuth.getUsername()).isEqualTo(auth.getUsername());
	}

	/**
	 * 試験対象：deleteUserメソッド
	 * 試験内容：
	 * ・投入したユーザーをdeleteできること
	 * ・deleteしたユーザーをselectした際にnullとなること
	 * ・例外が発生しないこと
	 */
	@Test
	void deleteUserTest() {
		/**
		 * テストデータの投入
		 */
		authenticationMapper.insertUser(auth);

		/**
		 * 試験
		 */
		authenticationMapper.deleteUser(auth.getUsername());
		Authentication auth2 = authenticationMapper.selectByUsername(auth.getUsername());
		assertThat(auth2).isNull();
	}
}
