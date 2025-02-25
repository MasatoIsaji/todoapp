package com.example.webapp.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDo {
	/** ToDoID */
	private Integer id;
	/** ToDoの持ち主 */
	private String username;
	/** 処理ステータス */
	private boolean status;
	/** ToDo */
	private String todo;
	/** 詳細 */
	private String detail;
	/** 優先順位 */
	private Integer priority;
	/** 作成日時 */
	private LocalDateTime createdAt;
	/** 更新日時 */
	private LocalDateTime updatedAt;
}