package com.example.webapp.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.webapp.entity.ToDo;
import com.example.webapp.exception.WebappException;
import com.example.webapp.form.ToDoForm;
import com.example.webapp.repository.ToDoMapper;

@ExtendWith(MockitoExtension.class)
class ToDoServiceImplTest {
	@Mock
	private ToDoMapper mapper;

	@InjectMocks
	private ToDoServiceImpl service;

	/*
	 * getUserListメソッドテスト
	 */
	@Nested
	class findAllToDoTest {
		/**
		 * 試験対象：findAllToDoメソッド
		 * 試験内容：
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("findAllToDo_正常系")
		void through() {
			/**
			 * mockセット
			 */
			List<ToDo> list = List.of(new ToDo());
			when(mapper.selectAll("username", true)).thenReturn(list);

			/**
			 * 試験
			 */
			assertDoesNotThrow(() -> service.findAllToDo("username", true));
		}
	}

	/*
	 * findByIdToDoメソッドテスト
	 */
	@Nested
	class findByIdToDoTest {
		/**
		 * 試験対象：findByIdToDoメソッド
		 * 試験内容：
		 * ・mapperで与えたToDoを返すこと
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("findByIdToDo_正常系")
		void doReturn() {
			/**
			 * mockセット
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(mapper.selectById(1)).thenReturn(todo);
			/**
			 * 試験
			 */
			ToDo result = service.findByIdToDo(1);
			assertDoesNotThrow(() -> service.findByIdToDo(1));
			assertAll(
					() -> assertEquals(todo.getId(), result.getId()),
					() -> assertEquals(todo.isStatus(), result.isStatus()),
					() -> assertEquals(todo.getTodo(), result.getTodo()),
					() -> assertEquals(todo.getDetail(), result.getDetail()),
					() -> assertEquals(todo.getCreatedAt(), result.getCreatedAt()),
					() -> assertEquals(todo.getUpdatedAt(), result.getUpdatedAt()));
		}

		/**
		 * 試験対象：findByIdToDoメソッド
		 * 試験内容：
		 * ・ToDoが存在しないこと(mapperでnullを返す)
		 * ・例外が発生すること
		 * ・エラーメッセージの内容が正しいこと
		 */
		@Test
		@DisplayName("findByIdToDo_異常系")
		void doThrow() {
			/**
			 * mockセット
			 */
			ToDo todo = null;
			when(mapper.selectById(1)).thenReturn(todo);
			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.findByIdToDo(1));
			try {
				service.findByIdToDo(1);
			} catch (WebappException e) {
				assertEquals("対象データがありません", e.getMessage());
			}
		}
	}

	/*
	 * insertToDoメソッドテスト
	 */
	@Nested
	class insertToDoTest {
		/**
		 * 試験対象：insertToDoメソッド
		 * 試験内容：
		 * ・正しいメッセージを返却すること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("insertToDo_正常系")
		void doReturn() {
			ToDoForm form = new ToDoForm(1, "username", false, "todo", "detail", true);
			String result = service.insertToDo(form);
			assertDoesNotThrow(() -> result);
			assertEquals(form.getTodo() + "が作成されました", result);
		}
	}

	/*
	 * updateToDoメソッドテスト
	 */
	@Nested
	class updateToDoTest {
		/**
		 * 試験対象：updateToDoメソッド
		 * 試験内容：
		 * ・正しいメッセージを返却すること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("updateToDo_正常系")
		void doReturn() {
			/**
			 * mock化
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(mapper.selectById(1)).thenReturn(todo);

			ToDoForm form = new ToDoForm(1, "username", false, "todo", "detail", true);
			String result = service.updateToDo(form);
			assertDoesNotThrow(() -> result);
			assertEquals(form.getTodo() + "が更新されました", result);
		}

		/**
		 * 試験対象：updateToDoメソッド
		 * 試験内容：
		 * ・正しいエラーメッセージを返却すること
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("updateToDo_異常系")
		void throwException() {
			/**
			 * mock化
			 */
			ToDo todo = null;
			when(mapper.selectById(1)).thenReturn(todo);

			ToDoForm form = new ToDoForm(1, "username", false, "todo", "detail", true);
			//			String result = service.updateToDo(form);
			assertThrows(WebappException.class, () -> service.updateToDo(form));
			try {
				service.updateToDo(form);
			} catch (WebappException e) {
				assertEquals("対象データがありません", e.getMessage());
			}
		}
	}

	/*
	 * updateStatusメソッドテスト
	 */
	@Nested
	class updateStatusTest {
		/**
		 * 試験対象：updateStatusメソッド
		 * 試験内容：
		 * ・完了に変更した場合の正しいメッセージを返却すること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("updateStatus_正常系(完了に変更)")
		void doReturn_unfinishTofinish() {
			/**
			 * mock化
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", false, "todo", "detail", time, time);
			when(mapper.selectById(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			String result = service.updateStatus(todo);
			assertDoesNotThrow(() -> result);
			assertEquals(todo.getTodo() + "を完了に移動しました", result);
		}

		/**
		 * 試験対象：updateStatusメソッド
		 * 試験内容：
		 * ・未完了に変更した場合の正しいメッセージを返却すること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("updateStatus_正常系(未完了に変更)")
		void doReturn_finishTounfinish() {
			/**
			 * mock化
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", true, "todo", "detail", time, time);
			when(mapper.selectById(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			String result = service.updateStatus(todo);
			assertDoesNotThrow(() -> result);
			assertEquals(todo.getTodo() + "を未完に移動しました", result);
		}

		/**
		 * 試験対象：updateStatusメソッド
		 * 試験内容：
		 * ・対象のToDoが存在しないこと
		 * ・正しいエラーメッセージを返却すること
		 * ・例外が発生すること
		 */
		@Test
		@DisplayName("updateStatus_異常系")
		void throwException() {
			/**
			 * mock化
			 */
			ToDo todo = null;
			when(mapper.selectById(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo2 = new ToDo(1, "username", true, "todo", "detail", time, time);
			assertThrows(WebappException.class, () -> service.updateStatus(todo2));
			try {
				service.updateStatus(todo2);
			} catch (WebappException e) {
				assertEquals("対象データがありません", e.getMessage());
			}
		}
	}

	/*
	 * deleteToDoメソッドテスト
	 */
	@Nested
	class deleteToDo {
		/**
		 * 試験対象：deleteToDoメソッド
		 * 試験内容：
		 * ・正しいメッセージを返却すること
		 * ・例外が発生しないこと
		 */
		@Test
		@DisplayName("deleteToDo_正常系")
		void doReturn_normal() {
			/**
			 * mock化
			 */
			LocalDateTime time = LocalDateTime.now();
			ToDo todo = new ToDo(1, "username", true, "todo", "detail", time, time);
			when(mapper.selectById(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			String result = service.deleteToDo(1);
			assertDoesNotThrow(() -> result);
			assertEquals(todo.getTodo() + "が削除されました", result);
		}

		/**
		 * 試験対象：deleteToDoメソッド
		 * 試験内容：
		 * ・削除対象が見つからず例外が発生すること
		 */
		@Test
		@DisplayName("deleteToDo_正常系")
		void doReturn_abnormal() {
			/**
			 * mock化
			 */
			ToDo todo = null;
			when(mapper.selectById(1)).thenReturn(todo);

			/**
			 * 試験
			 */
			assertThrows(WebappException.class, () -> service.deleteToDo(1));
			try {
				service.deleteToDo(1);
			} catch (WebappException e) {
				assertEquals("対象データがありません", e.getMessage());
			}
		}
	}
}
