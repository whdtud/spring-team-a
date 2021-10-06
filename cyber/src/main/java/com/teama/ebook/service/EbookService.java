package com.teama.ebook.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.teama.ebook.dto.EbookDTO;

public interface EbookService {
	//도서 검색
	public JSONObject ebookSearch(Map<String, Object> map) throws Exception;
	
	public int ebookAdd(Map<String, Object> map) throws Exception;
 	
	public List<EbookDTO> getEbookList();
	public int insertBook(Map<String, Object> map);

	public EbookDTO getEbook(int bookNo);
}
