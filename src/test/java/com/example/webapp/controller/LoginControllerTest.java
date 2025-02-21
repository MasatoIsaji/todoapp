package com.example.webapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoginController.class)
class LoginControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Nested
	class showLoginTest {

		/**
		 * 試験対象：showLoginメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showLogin_正常系")
		public void showLogin_normal() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(get("/login"))
					.andExpect(status().isOk());
		}
	}

	@Nested
	class showCreateUserFormTest {

		/**
		 * 試験対象：showCreateUserFormメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showCreateUserForm_正常系")
		public void showCreateUserForm_normal() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(get("/login/createUser"))
					.andExpect(status().isOk());
		}
	}

}
