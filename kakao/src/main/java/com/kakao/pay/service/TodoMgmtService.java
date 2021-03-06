package com.kakao.pay.service;

import java.util.List;
import java.util.Map;

import com.kakao.pay.model.TodoVO;

public interface TodoMgmtService {
	public List<TodoVO> getTodoList(String workId, int page);
	public Map<String, Object> createTodoWork(TodoVO todoVO);
	public Map<String, Object> updateTodoWork(TodoVO todoVO);
	public Map<String, Object> compelteTodoWork(TodoVO todoVO);
}
