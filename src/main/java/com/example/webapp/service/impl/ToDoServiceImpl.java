package com.example.webapp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.webapp.entity.ToDo;
import com.example.webapp.exception.WebappException;
import com.example.webapp.form.ToDoForm;
import com.example.webapp.helper.ToDoHelper;
import com.example.webapp.repository.ToDoMapper;
import com.example.webapp.service.ToDoService;

import lombok.RequiredArgsConstructor;

/**
 * ToDoサービス実装クラス
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

	private final ToDoMapper toDoMapper;

	@Override
	public List<ToDo> findAllToDo(String username, boolean status) {
		return toDoMapper.selectAll(username, status);
	}

	@Override
	public ToDo findByIdToDo(Integer id) {
		ToDo todo = toDoMapper.selectById(id);
		// 対象データが存在しない時
		if (todo == null) {
			throw new WebappException("対象データがありません");
		}
		return todo;
	}

	@Override
	public String insertToDo(ToDoForm form) {
		// entityへ変換
		ToDo toDo = ToDoHelper.convertToDo(form);
		// 登録実行
		toDoMapper.insert(toDo);

		return toDo.getTodo() + "が作成されました";
	}

	@Override
	public String updateToDo(ToDoForm form) {
		// entityへの変換
		ToDo toDo = ToDoHelper.convertToDo(form);

		// update対象が存在するか
		try {
			findByIdToDo(form.getId());
		} catch (WebappException e) {
			throw e;
		}

		toDoMapper.update(toDo);
		return toDo.getTodo() + "が更新されました";
	}

	@Override
	public String updateStatus(ToDo toDo) {
		// 対象ToDoが存在するか
		try {
			findByIdToDo(toDo.getId());
		} catch (WebappException e) {
			throw e;
		}

		// statusを反転させupdate
		toDo.setStatus(!toDo.isStatus());
		toDoMapper.update(toDo);

		if (toDo.isStatus()) {
			return toDo.getTodo() + "を完了に移動しました";
		} else {
			return toDo.getTodo() + "を未完に移動しました";
		}
	}

	@Override
	public String deleteToDo(Integer id) {
		ToDo target;
		try {
			// 対象ToDoが存在するか
			target = findByIdToDo(id);
		} catch (WebappException e) {
			throw e;
		}
		toDoMapper.delete(id);

		return target.getTodo() + "が削除されました";
	}

}
