package com.example.webapp.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.webapp.entity.Authentication;
import com.example.webapp.exception.WebappException;
import com.example.webapp.service.impl.AdminServiceImpl;

@WebMvcTest(AdminUserController.class)
class AdminUserControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	private AdminServiceImpl service;

	@InjectMocks
	private AdminUserController controller;

	@Nested
	class showUserListTest {

		/**
		 * 試験対象：showUserListメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showUserList_正常系")
		public void showUserList_normal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			Authentication auth = new Authentication("username", "password", "ADMIN");
			when(service.getUserList()).thenReturn(List.of(auth));

			/**
			 * 試験
			 */
			mockMvc.perform(get("/admin/userlist"))
					.andExpect(status().isOk())
					.andExpect(view().name("admin/userlist"))
					.andExpect(model().attributeExists("userlist"));
		}

		/**
		 * 試験対象：showUserListメソッド
		 * 試験内容：
		 * ・正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showUserList_異常系")
		public void showUserList_abnormal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			Authentication auth = new Authentication("username", "password", "ADMIN");
			when(service.getUserList()).thenThrow(new WebappException());

			/**
			 * 試験
			 */
			mockMvc.perform(get("/admin/userlist"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/"));
		}
	}

	@Nested
	class deleteUserTest {
		/**
		 * 試験対象：deleteUserメソッド
		 * 試験内容：
		 * ・正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("deleteUser_正常系")
		public void showUserList_normal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			String username = "username";
			when(service.deleteUser(anyString())).thenReturn("ユーザー：" + username + " 削除しました");

			/**
			 * 試験
			 */
			mockMvc.perform(get("/admin/delete/username"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/admin/userlist"))
					.andExpect(flash().attribute("message", "ユーザー：" + username + " 削除しました"));
		}

		/**
		 * 試験対象：deleteUserメソッド
		 * 試験内容：
		 * ・削除対象外ユーザーを選択し、リダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("deleteUser_異常系")
		public void showUserList_abnormal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			doThrow(new WebappException("adminは削除できません")).when(service).isNotDeleteUser("admin");
			/**
			 * 試験
			 */
			mockMvc.perform(get("/admin/delete/admin"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/admin/userlist"))
					.andExpect(flash().attribute("errorMessage", "adminは削除できません"));
		}
	}

	@Nested
	class registUserFormTest {
		/**
		 * 試験対象：registUserFormメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・modelがセットされていること
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("registUserForm_正常系")
		public void registUserForm_normal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			List<Authentication> resultList = List.of(new Authentication());
			when(service.getUserList()).thenReturn(resultList);

			/**
			 * 試験
			 */
			mockMvc.perform(get("/admin/userRegistForm"))
					.andExpect(status().isOk())
					.andExpect(view().name("admin/registUserForm"))
					.andExpect(model().attributeExists("authentication"));
		}

		/**
		 * 試験対象：registUserFormメソッド
		 * 試験内容：
		 * ・ユーザー数上限となること
		 * ・正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("registUserForm_異常系")
		public void registUserForm_abnormal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			List<Authentication> userList = Mockito.mock(List.class);
			when(service.getUserList()).thenReturn(userList);
			when(userList.size()).thenReturn(50);

			/**
			 * 試験
			 */
			mockMvc.perform(get("/admin/userRegistForm"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/admin/userlist"))
					.andExpect(flash().attribute("errorMessage", "ユーザー数が上限に達しているため追加できません"));
		}
	}

	@Nested
	class registUserTest {
		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・フラッシュメッセージがセットされていること
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("registUser_正常系")
		public void registUser_normal() throws Exception {
			/**
			 * mock化
			 */
			Authentication authentication = Mockito.mock(Authentication.class);
			when(authentication.getUsername()).thenReturn("testuser");
			when(authentication.getPassword()).thenReturn("password");
			when(service.registUser(Mockito.any(Authentication.class))).thenReturn("ユーザー登録が完了しました。");

			/*
			 * 試験
			 */
			mockMvc.perform(post("/admin/registUser")
					.with(csrf())
					.param("username", "testuser")
					.param("password", "password")
					.param("authority", "ADMIN"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/admin/userlist"))
					.andExpect(flash().attribute("message", "ユーザー登録が完了しました。"));
		}

		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・ユーザーが重複し、リダイレクトパスを返すこと
		 * ・フラッシュメッセージがセットされていること
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("registUser_異常系01")
		public void registUser_abnormal01() throws Exception {
			/**
			 * mock化
			 */
			doThrow(new WebappException("既に使われているユーザー名です")).when(service).isRegistUser("testuser");

			/**
			 * 試験
			 */
			mockMvc.perform(post("/admin/registUser")
					.with(csrf())
					.param("username", "testuser")
					.param("password", "password")
					.param("authority", "ADMIN"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/admin/userlist"))
					.andExpect(flash().attribute("errorMessage", "既に使われているユーザー名です"));
		}

		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・バリデーションエラーとなること
		 * ・フラッシュメッセージがセットされていること
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("registUser_異常系02")
		public void registUser_abnormal02() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(post("/admin/registUser")
					.param("username", "")
					.param("password", "password")
					.param("authority", "USER")
					.with(csrf()))
					.andExpect(status().isOk())
					.andExpect(view().name("admin/registUserForm"));
		}
	}

}
