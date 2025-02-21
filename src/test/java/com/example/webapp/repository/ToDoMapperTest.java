package com.example.webapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import com.example.webapp.entity.ToDo;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ToDoMapperTest {

	@Autowired
	private ToDoMapper mapper;

	// 使用するentityのテストデータ
	// idがオートインクリメントのためそれ以外をセット
	LocalDateTime time = LocalDateTime.now();
	ToDo todo1false = ToDo.builder()
			.username("username")
			.status(false)
			.todo("todo")
			.detail("detail")
			.createdAt(time)
			.updatedAt(time)
			.build();

	ToDo todo1true = ToDo.builder()
			.username("username")
			.status(true)
			.todo("todo")
			.detail("detail")
			.createdAt(time)
			.updatedAt(time)
			.build();

	/**
	 * 試験対象：selectAllメソッド
	 * 試験内容：
	 * ・投入したユーザーが取得できること
	 * ・完了、未完了それぞれ正しくデータが取得できること
	 * ・例外が発生しないこと
	 */
	@Test
	void selectAllTest() {
		/**
		 * テストデータの投入
		 * idがオートインクリメントのため同じデータを投入しても違うidfが振られる
		 */
		// 未完了データ
		mapper.insert(todo1false);
		mapper.insert(todo1false);
		mapper.insert(todo1false);
		// 完了データ
		mapper.insert(todo1true);
		mapper.insert(todo1true);
		mapper.insert(todo1true);

		/**
		 * 試験
		 */
		// 未完了データ取得
		List<ToDo> result = mapper.selectAll("username", false);
		assertDoesNotThrow(() -> result);
		assertTrue(result.size() >= 3);

		// 完了データ
		List<ToDo> resultFinish = mapper.selectAll("username", true);
		assertDoesNotThrow(() -> resultFinish);
		assertTrue(resultFinish.size() >= 3);
	}

	/**
	 * 試験対象：selectByIdメソッド
	 * 試験内容：
	 * ・取得結果がnullでないこと
	 * ・例外が発生しないこと
	 */
	@Test
	void selectByIdTest() {
		/**
		 * テストデータの投入
		 */
		mapper.insert(todo1false);

		/**
		 * 試験
		 */
		ToDo result = mapper.selectById(1);
		assertDoesNotThrow(() -> result);
		assertNotNull(result);
	}

	/**
	 * 試験対象：insertメソッド
	 * 試験内容：
	 * ・テストデータ投入前と後を比較して数が+1されていること
	 * ・例外が発生しないこと
	 */
	@Test
	void insertTest() {
		// 投入前サイズ
		int beforeInsertSize = mapper.selectAll("username", false).size();
		/**
		 * テストデータの投入
		 */
		mapper.insert(todo1false);
		// 投入後サイズ
		int afterInsertSize = mapper.selectAll("username", false).size();

		/**
		 * 試験
		 */
		assertDoesNotThrow(() -> mapper.insert(todo1false));
		assertEquals(beforeInsertSize, afterInsertSize - 1);
	}

	/**
	 * 試験対象：updateメソッド
	 * 試験内容：
	 * ・update後、データが変更されていること
	 * ・例外が発生しないこと
	 */
	@Test
	void updateTest() {
		/**
		 * テストデータの投入
		 */
		// ターゲットデータを取得
		ToDo target = mapper.selectById(1);
		target.setTodo("todi");
		mapper.update(target);

		/**
		 * 試験
		 */
		ToDo result = mapper.selectById(1);
		assertDoesNotThrow(() -> mapper.update(result));
		assertEquals(target.getTodo(), result.getTodo());
	}

	/**
	 * 試験対象：deleteメソッド
	 * 試験内容：
	 * ・delete後、データが存在しないこと
	 * ・例外が発生しないこと
	 */
	@Test
	void deleteTest() {
		/**
		 * テストデータの投入
		 */
		mapper.insert(todo1false);

		/**
		 * 試験
		 */
		mapper.delete(1);
		ToDo rsult = mapper.selectById(1);
		assertDoesNotThrow(() -> mapper.delete(1));
		assertNull(rsult);
	}
}
