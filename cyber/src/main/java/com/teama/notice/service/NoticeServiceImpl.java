package com.teama.notice.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teama.notice.dao.NoticeDAO;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeDAO noticeDAO;
	
	//게시글 불러오기
	public List<Map<String, Object>> noticeList(Map<String, Object> map) {
		return noticeDAO.noticeList(map);
	}
	
	//페이징 totalCount
	public int totalCount(Map<String, Object> map) {
		return noticeDAO.totalCount(map);
	}
	
	//게시글 상세보기
	@Override
	public Map<String, Object> detail(Map<String, Object> map) {
		return noticeDAO.detail(map);
	}
	
	//게시글 글쓰기
	
	//게시글 삭제
	@Override
	public int delete(Map<String, Object> map) {
		return noticeDAO.delete(map);
	}
	
	//게시글 수정
	
	//게시글 조회수
	@Override
	public int count(Map<String, Object> map) {
		return noticeDAO.count(map);
	}
	
}
