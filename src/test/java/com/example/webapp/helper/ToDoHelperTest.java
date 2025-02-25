package com.example.webapp.helper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.example.webapp.entity.ToDo;
import com.example.webapp.form.ToDoForm;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ToDoHelperTest {

	// 使用するインスタンス
	ToDoForm form = new ToDoForm(1, "admin", true, "ToDo", "detail",1, true);
	ToDo todo = new ToDo(1, "admin", true, "ToDo", "detail", 1, LocalDateTime.now(), LocalDateTime.now());

	/*
	 * converToDoメソッドのテスト
	 */
	@Nested
	class convertToDo {
		/**
		 * 試験対象：convertToDoメソッド
		 * 試験内容：
		 * ・ToDoFormクラスが正しくToDoクラスに変換できること
		 * ・例外が発生しないこと
		 * 備考：
		 * ・変換前のToDoFormクラスはバリデーションを通過しているため値の不備については試験しない
		 */
		@Test
		@DisplayName("convertToDo_正常系")
		void doConvert() {
			ToDo result = ToDoHelper.convertToDo(form);

			assertEquals(form.getId(), result.getId());
			assertEquals(form.getUsername(), result.getUsername());
			assertEquals(false, result.isStatus()); // ToDo登録/編集時に必ずfalseをセットするためfalseで試験
			assertEquals(form.getTodo(), result.getTodo());
			assertEquals(form.getDetail(), result.getDetail());
			assertDoesNotThrow(() -> ToDoHelper.convertToDo(form));
		}
	}

	/*
	 * convertToDoFormメソッドのテスト
	 */
	@Nested
	class convertToDoForm {
		/**
		 * 試験対象：convertToDoFormメソッド
		 * 試験内容：
		 * ・ToDoクラスが正しくToDoFormクラスに変換できること
		 * ・例外が発生しないこと
		 * 備考：
		 * ・変換前のToDoクラスはバリデーションを通過しているため値の不備については試験しない
		 */
		@Test
		@DisplayName("convertToDoForm_正常系")
		void doConvert() {
			ToDoForm result = ToDoHelper.convertToDoForm(todo);

			assertEquals(todo.getId(), result.getId());
			assertEquals(todo.getUsername(), result.getUsername());
			assertEquals(false, result.isStatus()); // ToDo登録/編集時に必ずfalseをセットするためfalseで試験
			assertEquals(todo.getTodo(), result.getTodo());
			assertEquals(todo.getDetail(), result.getDetail());
			assertDoesNotThrow(() -> ToDoHelper.convertToDoForm(todo));
		}
	}

}
