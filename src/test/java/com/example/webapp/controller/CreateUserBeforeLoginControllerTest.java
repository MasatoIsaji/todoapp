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

@WebMvcTest(CreateUserBeforeLoginController.class)
class CreateUserBeforeLoginControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private AdminServiceImpl service;

	@InjectMocks
	private CreateUserBeforeLoginController controller;

	@Nested
	class registUserTest {
		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・正常にユーザー登録が完了し、正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showUserList_正常系")
		public void registUser_normal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			List<Authentication> userList = Mockito.mock(List.class);
			when(service.getUserList()).thenReturn(userList);
			when(userList.size()).thenReturn(10);
			/**
			 * 試験
			 */
			mockMvc.perform(post("/createUserbeforeLogin/registUser").with(csrf())
					.param("username", "testuser")
					.param("password", "password")
					.param("authority", "ADMIN"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/login"))
					.andExpect(flash().attribute("message", "testuser を登録しました。"));
		}

		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・バリデーションエラーが発生し、正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showUserList_異常系01")
		public void registUser_abnormal01() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			/**
			 * 試験
			 */
			mockMvc.perform(post("/createUserbeforeLogin/registUser").with(csrf())
					.param("username", "")
					.param("password", "")
					.param("authority", ""))
					.andExpect(status().isOk())
					.andExpect(view().name("createUserbeforeLogin/createUser"));
		}

		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・ユーザー重複が発生し、正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showUserList_異常系02")
		public void registUser_abnormal02() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */
			doThrow(WebappException.class).when(service).isRegistUser(anyString());
			/**
			 * 試験
			 */
			mockMvc.perform(post("/createUserbeforeLogin/registUser").with(csrf())
					.param("username", "testuser")
					.param("password", "password")
					.param("authority", "ADMIN"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/login/createUser"));
		}

		/**
		 * 試験対象：registUserメソッド
		 * 試験内容：
		 * ・ユーザー数が登録上限に達し、正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showUserList_異常系03")
		public void registUser_abnormal03() throws Exception {
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
			mockMvc.perform(post("/createUserbeforeLogin/registUser").with(csrf())
					.param("username", "testuser")
					.param("password", "password")
					.param("authority", "ADMIN"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/login/createUser"))
					.andExpect(flash().attribute("errorMessage", "ユーザー数が上限に達しているため追加できません。"));
		}

	}

}
