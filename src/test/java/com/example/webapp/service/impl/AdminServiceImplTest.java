package com.example.webapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.webapp.entity.Authentication;
import com.example.webapp.exception.WebappException;
import com.example.webapp.repository.AuthenticationMapper;
import com.example.webapp.utility.PasswordGenerator;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
	@Mock
	private AuthenticationMapper authenticationMapper;

	@InjectMocks
	private AdminServiceImpl service;

	/*
	 * getUserListメソッドテスト
	 */
	@Nested
	class getUserListMethodTest {
		/**
		 * 試験対象：getUserListメソッド
		 * 試験内容：
		 * ・Mapperからの取得値とMapperに与えた値が同値となること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("getUserList_正常系(SELECT結果有り)")
		void doConvertOne() {
			/**
			 * mockセット
			 */
			// select結果を用意
			Authentication auth = new Authentication("username", "password", "ADMIN");
			List<Authentication> selectedList = new ArrayList<>();
			selectedList.add(auth);

			when(authenticationMapper.selectUserList()).thenReturn(selectedList);

			/**
			 * 試験
			 */
			List<Authentication> resultList = service.getUserList();
			assertDoesNotThrow(() -> service.getUserList());
			assertEquals(selectedList.get(0), resultList.get(0));
		}

		/**
		 * 試験対象：getUserListメソッド
		 * 試験内容：
		 * ・Mapperからの取得値がnullになること
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("getUserList_異常系(SELECT結果null)")
		void throwException() {
			/**
			 * mockセット
			 */
			// select結果を用意
			List<Authentication> selectedList = null;

			when(authenticationMapper.selectUserList()).thenReturn(selectedList);

			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.getUserList());
		}
	}

	/*
	 * registUserメソッドテスト
	 */
	@Nested
	class registUserTest {
		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("registUser_正常系")
		void doesNotThrow() {
			/**
			 * mockセット
			 */
			Authentication auth = new Authentication("username", "password", "ADMIN");

			// staticメソッドをmock化
			MockedStatic<PasswordGenerator> mockedPG = mockStatic(PasswordGenerator.class);
			mockedPG.when(() -> PasswordGenerator.generateHashedPassword("password")).thenReturn(auth.getPassword());

			/**
			* 試験
			*/
			assertDoesNotThrow(() -> service.registUser(auth));
		}
	}

	/*
	 * deleteUserメソッドテスト
	 */
	@Nested
	class deleteUserTest {
		/**
		 * 試験対象：deleteUserメソッド
		 * 試験内容：
		 * ・戻り値のメッセージが期待値と等しいこと
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("deleteUser_正常系")
		void doesNotThrow() {
			/**
			 * mockセット
			 */
			Authentication auth = new Authentication("username", "password", "ADMIN");
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);
			/**
			 * 試験
			 */
			String resultString = service.deleteUser("username");
			assertDoesNotThrow(() -> service.deleteUser("username"));
			assertEquals("ユーザー：username 削除しました", resultString);
		}

		/**
		 * 試験対象：deleteUserメソッド
		 * 試験内容：
		 * ・戻り値のメッセージが期待値と等しいこと
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("deleteUser_異常系")
		void throwsException() {
			/**
			 * mockセット
			 */
			Authentication auth = null;
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);
			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.deleteUser("username"));
			try {
				service.deleteUser("username");
			} catch (WebappException e) {
				assertEquals("削除対象が存在しません", e.getMessage());
			}
		}
	}

	/*
	 * isNotDeleteUserメソッドテスト
	 */
	@Nested
	@SpringBootTest
	@WithMockUser(username = "username", password = "password", roles = "USER")
	class isNotDeleteUserTest {
		/**
		 * 試験対象：isNotDeleteUserメソッド
		 * 試験内容：
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("isNotDeleteUser正常系")
		void checkThrough_normal01() {
			/**
			 * mockセット
			 */
			// @SpringBootTest+@WithMockUserで認証ユーザーを設定
			/**
			 * 試験
			 */
			assertDoesNotThrow(() -> service.isNotDeleteUser("testuser"));
		}

		/**
		 * 試験対象：isNotDeleteUserメソッド
		 * 試験内容：
		 * ・引数にadminを設定し、戻り値のメッセージが期待値と等しいこと
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("isNotDeleteUser異常系01")
		void checkMessage_abNormal01() {
			/**
			 * mockセット
			 */
			// @SpringBootTest+@WithMockUserで認証ユーザーを設定
			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.isNotDeleteUser("admin"));
			try {
				service.isNotDeleteUser("testuser");
			} catch (WebappException e) {
				assertEquals("adminは削除できません", e.getMessage());
			}
		}

		/**
		 * 試験対象：isNotDeleteUserメソッド
		 * 試験内容：
		 * ・引数とカレントユーザーを同名にし、戻り値のメッセージが期待値と等しいこと
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("isNotDeleteUser異常系02")
		void checkMessage_abNormal02() {
			/**
			 * mockセット
			 */
			// @SpringBootTest+@WithMockUserで認証ユーザーを設定
			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.isNotDeleteUser("username"));
			try {
				service.isNotDeleteUser("username");
			} catch (WebappException e) {
				assertEquals("操作中のユーザーは削除できません", e.getMessage());
			}
		}
	}

	/*
	 * isRegistUserメソッドテスト
	 */
	@Nested
	class isRegistUserTest {
		/**
		 * 試験対象：isRegistUserメソッド
		 * 試験内容：
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("isNotDeleteUser正常系")
		void checkThrough_normal() {
			/**
			 * mockセット
			 */
			Authentication auth = new Authentication("username", "password", "ADMIN");
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);
			/**
			 * 試験
			 */
			assertDoesNotThrow(() -> service.isRegistUser("username"));
		}

		/**
		 * 試験対象：isRegistUserメソッド
		 * 試験内容：
		 * ・エラーメッセージが期待値通りであること
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("isNotDeleteUser異常系")
		void checkThrough_abNormal() {
			/**
			 * mockセット
			 */
			Authentication auth = null;
			when(authenticationMapper.selectByUsername(anyString())).thenReturn(auth);
			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.isRegistUser("username"));
			try {
				service.isRegistUser("username");
			} catch (WebappException e) {
				assertEquals("既に使われているユーザー名です", e.getMessage());
			}
		}
	}

}
