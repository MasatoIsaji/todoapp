package com.example.webapp.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.webapp.entity.ToDo;
import com.example.webapp.exception.WebappException;
import com.example.webapp.form.ToDoForm;
import com.example.webapp.service.ToDoService;

@WebMvcTest(ToDoController.class)
class ToDoControllerTest {
	@Autowired
	MockMvc mockMvc;

	@MockBean
	private ToDoService service;

	@InjectMocks
	private ToDoController controller;

	@Nested
	class showUserListTest {
		/**
		 * 試験対象：listメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・完了と未完了がmodelにセットされていること
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("list_正常系")
		public void list_normal() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos"))
					.andExpect(status().isOk())
					.andExpect(view().name("todo/list"))
					.andExpect(model().attributeExists("todosUnfinished"))
					.andExpect(model().attributeExists("todosFinished"));
		}
	}

	@Nested
	class detailTest {
		/**
		 * 試験対象：detailメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("detail_正常系")
		public void detail_normal() throws Exception {
			/**
			 * mockセット
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(service.findByIdToDo(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/1"))
					.andExpect(status().isOk())
					.andExpect(view().name("todo/detail"));
		}

		/**
		 * 試験対象：detailメソッド
		 * 試験内容：
		 * ・サービスから例外が発生し、リダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("detail_異常系")
		public void detail_abnormal() throws Exception {
			/**
			 * mockセット
			 */
			//			LocalDateTime time = LocalDateTime.now();
			//			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			doThrow(new WebappException("対象データがありません")).when(service).findByIdToDo(1);

			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/1"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"))
					.andExpect(flash().attribute("errorMessage", "対象データがありません"));
		}
	}

	@Nested
	class newToDoTest {
		/**
		 * 試験対象：newToDoメソッド
		 * 試験内容：
		 * ・正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("newToDo_正常系")
		public void detail_normal() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/form"))
					.andExpect(status().isOk())
					.andExpect(model().attribute("toDoForm", hasProperty("isNew", is(true))));
		}
	}

	@Nested
	class createTest {
		/**
		 * 試験対象：createメソッド
		 * 試験内容：
		 * ・一覧への正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("create_正常系")
		public void create_normal() throws Exception {
			/**
			 * mockセット
			 */
			ToDoForm form = new ToDoForm(1, "username", false, "todo", "detail", true);
			when(service.insertToDo(form)).thenReturn("usernameが作成されました");

			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/save")
					.with(csrf())
					.param("id", "1")
					.param("username", "username")
					.param("status", "true")
					.param("todo", "todo")
					.param("detail", "detail")
					.param("isNew", "true"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"));
		}

		/**
		 * 試験対象：createメソッド
		 * 試験内容：
		 * ・バリデーションエラーが発生し、formへのパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("create_異常系01")
		public void create_abnormal01() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/save")
					.with(csrf())
					.param("id", "")
					.param("username", "")
					.param("status", "")
					.param("todo", "")
					.param("detail", "")
					.param("isNew", ""))
					.andExpect(view().name("todo/form"));
		}
	}

	@Nested
	class editTest {
		/**
		 * 試験対象：editメソッド
		 * 試験内容：
		 * ・formへの正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("edit_正常系")
		public void edit_normal() throws Exception {
			/**
			 * mockセット
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(service.findByIdToDo(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/edit/1").with(csrf()))
					.andExpect(status().isOk())
					.andExpect(view().name("todo/form"))
					.andExpect(model().attributeExists("toDoForm"));
		}

		/**
		 * 試験対象：editメソッド
		 * 試験内容：
		 * ・一覧へのリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("edit_正常系")
		public void edit_abnormal() throws Exception {
			/**
			 * mockセット
			 */
			doThrow(new WebappException("対象データがありません")).when(service).findByIdToDo(anyInt());

			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/edit/1").with(csrf()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"))
					.andExpect(flash().attribute("errorMessage", "対象データがありません"));
		}
	}

	@Nested
	class updateTest {
		/**
		 * 試験対象：updateメソッド
		 * 試験内容：
		 * ・一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("update_正常系")
		public void update_normal() throws Exception {
			/**
			 * mockセット
			 */
			ToDoForm form = new ToDoForm(1, "username", false, "todo", "detail", true);
			when(service.updateToDo(form)).thenReturn("todoが更新されました");

			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/update").with(csrf())
					.param("id", "1")
					.param("username", "username")
					.param("status", "true")
					.param("todo", "todo")
					.param("detail", "detail")
					.param("isNew", "true"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"));
		}

		/**
		 * 試験対象：updateメソッド
		 * 試験内容：
		 * ・一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("update_異常系01")
		public void update_abnormal01() throws Exception {
			/**
			 * mockセット
			 */
			ToDoForm form = new ToDoForm(1, "username", false, "todo", "detail", true);
			doThrow(new WebappException("対象データがありません")).when(service).updateToDo(form);

			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/update").with(csrf())
					.param("id", "1")
					.param("username", "username")
					.param("status", "true")
					.param("todo", "todo")
					.param("detail", "detail")
					.param("isNew", "true"))
					.andExpect(status().is3xxRedirection());

		}

		/**
		 * 試験対象：updateメソッド
		 * 試験内容：
		 * ・フォームへの正しいパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("update_異常系02")
		public void update_abnormal02() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/update").with(csrf())
					.param("id", "")
					.param("username", "")
					.param("status", "")
					.param("todo", "")
					.param("detail", "")
					.param("isNew", ""))
					.andExpect(status().isOk())
					.andExpect(view().name("todo/form"));
		}
	}

	@Nested
	class updateStatusTest {
		/**
		 * 試験対象：updateStatusメソッド
		 * 試験内容：
		 * ・一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("updateStatus_正常系")
		public void updateStatus_normal() throws Exception {
			/**
			 * mockセット
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(service.updateStatus(todo)).thenReturn("メッセージ");

			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/update/status").with(csrf())
					.param("id", "1")
					.param("username", "username")
					.param("status", "true")
					.param("todo", "todo")
					.param("detail", "detail")
					.param("createdAt", "2025/02/20 16:07:29.325")
					.param("updatedAt", "2025/02/20 16:07:29.325"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"));
		}

		/**
		 * 試験対象：updateStatusメソッド
		 * 試験内容：
		 * ・サービスで例外が発生し、一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("updateStatus_異常系01")
		public void updateStatus_abnormal01() throws Exception {
			/**
			 * mockセット
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			doThrow(new WebappException("NG")).when(service).updateStatus(todo);

			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/update/status").with(csrf())
					.param("id", "1")
					.param("username", "username")
					.param("status", "true")
					.param("todo", "todo")
					.param("detail", "detail")
					.param("createdAt", "2025/02/20 16:07:29.325")
					.param("updatedAt", "2025/02/20 16:07:29.325"))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"));
		}

		/**
		 * 試験対象：updateStatusメソッド
		 * 試験内容：
		 * ・バリデーションエラーが発生し、一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("updateStatus_異常系02")
		public void updateStatus_abnormal02() throws Exception {
			/**
			 * 試験
			 */
			mockMvc.perform(post("/todos/update/status").with(csrf())
					.param("id", "")
					.param("username", "")
					.param("status", "")
					.param("todo", "")
					.param("detail", "")
					.param("createdAt", "")
					.param("updatedAt", ""))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"));
		}
	}

	@Nested
	class deleteTest {
		/**
		 * 試験対象：deleteメソッド
		 * 試験内容：
		 * ・一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("delete_正常系")
		public void delete_normal() throws Exception {
			/**
			 * mockセット
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(service.deleteToDo(1)).thenReturn("メッセージ");

			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/delete/1").with(csrf()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"))
					.andExpect(flash().attribute("message", "メッセージ"));
		}

		/**
		 * 試験対象：deleteメソッド
		 * 試験内容：
		 * ・サービスで例外が発生し、一覧への正しいリダイレクトパスを返すこと
		 * ・例外が発生しないこと
		 * @throws Exception MockMvcから発生する例外(業務例外ではない)
		 */
		@Test
		@WithMockUser(username = "username", roles = "ADMIN")
		@DisplayName("delete_異常系")
		public void delete_abnormal01() throws Exception {
			/**
			 * mockセット
			 */
			doThrow(new WebappException("メッセージ")).when(service).deleteToDo(1);

			/**
			 * 試験
			 */
			mockMvc.perform(get("/todos/delete/1").with(csrf()))
					.andExpect(status().is3xxRedirection())
					.andExpect(redirectedUrl("/todos"))
					.andExpect(flash().attribute("errorMessage", "メッセージ"));
		}
	}

}
