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

@WebMvcTest(MenuController.class)
class MenuControllerTest {
	@Autowired
	MockMvc mockMvc;

	@Nested
	class showUserListTest {

		/**
		 * 試験対象：showMenuメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "testuser", roles = "ADMIN")
		@DisplayName("showMenu_正常系")
		public void showMenu_normal() throws Exception {
			/**
			 * mockセット
			 * SpringSecurityの認証は@WithMockUserで通す
			 */

			/**
			 * 試験
			 */
			mockMvc.perform(get("/"))
					.andExpect(status().isOk());
		}
	}

}
