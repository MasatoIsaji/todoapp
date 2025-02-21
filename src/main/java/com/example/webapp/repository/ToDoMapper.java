package com.example.webapp.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.webapp.entity.ToDo;

/**
 * ToDo：リポジトリ
 */
@Mapper
public interface ToDoMapper {

	/**
	 * 全てのToDoを取得します。
	 */
	List<ToDo> selectAll(@Param("username") String username, @Param("status") boolean status);

	/**
	 * 指定されたIDのToDoを取得します。
	 */
	ToDo selectById(@Param("id") Integer id);

	/**
	 * ToDoを登録します。
	 */
	void insert(ToDo toDo);

	/**
	 * ToDoを更新します。
	 */
	void update(ToDo toDo);

	/**
	 * 指定されたIDのToDoを削除します。
	 */
	void delete(@Param("id") Integer id);
}