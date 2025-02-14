package com.example.webapp.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * すること：エンティティ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {
	/** することID */
	private Integer id;
	/** することの持ち主 */
	private String username;
	/** 処理ステータス */
	private boolean status;
	/** すること */
	private String todo;
	/** すること詳細 */
	private String detail;
	/** 作成日時 */
	private LocalDateTime createdAt;
	/** 更新日時 */
	private LocalDateTime updatedAt;
}