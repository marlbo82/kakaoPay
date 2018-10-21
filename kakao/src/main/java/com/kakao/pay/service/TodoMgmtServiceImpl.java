package com.kakao.pay.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kakao.pay.dao.TodoMgmtDao;
import com.kakao.pay.model.TodoVO;

@Service("com.kakao.pay.service.TodoMgmtService")
public class TodoMgmtServiceImpl implements TodoMgmtService {
	
	@Autowired
	private TodoMgmtDao todoMgmtDao;
	
	@Override
	public List<TodoVO> getTodoList(String workId, int page) {
		List<TodoVO> result = new ArrayList<TodoVO>();
		
		if (!StringUtils.isEmpty(workId)) {
			TodoVO todoVO = todoMgmtDao.getTodoList(workId);
			result.add(todoVO);
		} else {
			Set<String> keys = todoMgmtDao.getAllKeys();
			Object[] keyArr = keys.toArray();
			Arrays.sort(keyArr);
			
			getTodoListByPaging(page, result, keyArr);
		}

        return result;
	}
	
	@Override
	public Map<String, Object> createTodoWork(TodoVO todoVO) {
		Set<String> keys = todoMgmtDao.getAllKeys();
		
		Object[] keyArr = keys.toArray();
		int newKey = 1;
		if (keyArr.length > 0) {
			Arrays.sort(keyArr);
			newKey = (Integer.parseInt((String) keyArr[keyArr.length-1])) + 1;			
		}

		int resultCode = 500;
		String resultMsg = "Internal Server Error";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		boolean existUprWorkId = false; 
		
		if (!StringUtils.isEmpty(todoVO.getUprWorkId())) {
			for (int i = 0; i < keyArr.length; i++) {
				TodoVO todo = todoMgmtDao.getTodoList(keyArr[i]+"");
				if (todoVO.getUprWorkId().equals(todo.getWorkId())) {
					existUprWorkId = true;
				}
			}
		} else {
			existUprWorkId = true;			
		}
		
		if (!existUprWorkId) {
			result.put("resultCode", 400);
			result.put("resultMsg", "Bad Request - 참조 불가능한 ID 입니다.");
			return result;
		}
		
        Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workId", newKey+"");
		paramMap.put("workTitle", todoVO.getWorkTitle());
		paramMap.put("uprWorkId", StringUtils.isEmpty(todoVO.getUprWorkId())?null:todoVO.getUprWorkId());
		paramMap.put("firstRegDtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramMap.put("lastModDtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramMap.put("completeDtm", null);
		
		todoMgmtDao.createTodoWork(newKey, paramMap);

		result.put("resultCode", 200);
		result.put("resultMsg", "Success");
		
		return result;
	}

	@Override
	public Map<String, Object> updateTodoWork(TodoVO todoVO) {
		Set<String> keys = todoMgmtDao.getAllKeys();
		Object[] keyArr = keys.toArray();
		Arrays.sort(keyArr);
		
		int resultCode = 500;
		String resultMsg = "Internal Server Error";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (todoVO.getWorkId().equals(todoVO.getUprWorkId())) {
			result.put("resultCode", 400);
			result.put("resultMsg", "Bad Request - 셀프 참조는 불가능 합니다.");
			return result;
		}
		
		for (int i = 0; i < keyArr.length; i++) {
			TodoVO todo = todoMgmtDao.getTodoList(keyArr[i]+"");
			if (todoVO.getWorkId().equals(todo.getUprWorkId()) && todoVO.getUprWorkId().equals(todo.getWorkId())) {
				result.put("resultCode", 400);
				result.put("resultMsg", "Bad Request - 서로 참조는 불가능 합니다.");
				return result;
			}
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workId", todoVO.getWorkId());
		paramMap.put("workTitle", todoVO.getWorkTitle());
		paramMap.put("uprWorkId", todoVO.getUprWorkId());
		paramMap.put("firstRegDtm", todoVO.getFirstRegDtm());
		paramMap.put("lastModDtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramMap.put("completeDtm", null);
		
		todoMgmtDao.updateTodoWork(paramMap);
		
		result.put("resultCode", 200);
		result.put("resultMsg", "Success");
		
		return result;
	}
	
	@Override
	public Map<String, Object> compelteTodoWork(TodoVO todoVO) {
		Set<String> keys = todoMgmtDao.getAllKeys();
		Object[] keyArr = keys.toArray();
		Arrays.sort(keyArr);
		
		int resultCode = 500;
		String resultMsg = "Internal Server Error";
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		for (int i = 0; i < keyArr.length; i++) {
			TodoVO todo = todoMgmtDao.getTodoList(keyArr[i]+"");
			if (todoVO.getWorkId().equals(todo.getUprWorkId()) && todo.getCompleteDtm() == null) {
				result.put("resultCode", 400);
				result.put("resultMsg", "Bad Request - 미완료 참조가 남아 있습니다.");
				return result;
			}
		}
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workId", todoVO.getWorkId());
		paramMap.put("workTitle", todoVO.getWorkTitle());
		paramMap.put("uprWorkId", todoVO.getUprWorkId());
		paramMap.put("firstRegDtm", todoVO.getFirstRegDtm());
		paramMap.put("lastModDtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		paramMap.put("completeDtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		
		todoMgmtDao.updateTodoWork(paramMap);
		
		result.put("resultCode", 200);
		result.put("resultMsg", "Success");
		
		return result;
	}
	
	private void getTodoListByPaging(int page, List<TodoVO> result, Object[] keyArr) {
		String workId;
		int cntOfOnePage = 4;
		int totalPage = keyArr.length/cntOfOnePage;
		if((keyArr.length % cntOfOnePage) > 0) {
			totalPage += 1;
		}
		
		int start = (page-1) * cntOfOnePage;
		int end = page * cntOfOnePage;
		if(end > keyArr.length) {
			end = keyArr.length;
		}
		
		for (int i = start; i < end; i++) {
			workId = (String) keyArr[i];
			TodoVO todoVO = todoMgmtDao.getTodoList(workId);
			todoVO.setTotalPage(totalPage);
			result.add(todoVO);
		}
	}
}