package com.teama.admin.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teama.common.CommandMap;
import com.teama.loan.service.LoanService;
import com.teama.storage.dto.BookStorageDTO;
import com.teama.storage.dto.BookStorageViewDTO;
import com.teama.storage.service.BookStorageService;
import com.teama.util.PaginationTagServlet;
import com.teama.util.Util;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
@RequestMapping("/admin/storage")
public class AdminBookStorageController {

	@Resource(name="bookStorageService")
	private BookStorageService bookStorageService;
	@Resource(name="loanService")
	private LoanService loanService;
	
	@RequestMapping("home.do")
	public ModelAndView home(CommandMap commandMap) {
		ModelAndView mv = new ModelAndView("admin/bookStorage");

		int currentPageNo = commandMap.getIntValue("pageNo", 1);
		int totalRecordCount = bookStorageService.getTotalCount();
		
		PaginationInfo paginationInfo = Util.newPaginationInfo(currentPageNo, totalRecordCount);
		
		int firstRecordIndex = paginationInfo.getFirstRecordIndex();
		int recordCountPerPage = paginationInfo.getRecordCountPerPage();
		
		List<Map<String, Object>> bookStorageViewDTOList = bookStorageService.getViewMapList(firstRecordIndex, recordCountPerPage);
		mv.addObject("bookStorageViewDTOList", bookStorageViewDTOList);
		mv.addObject("paginationInfo", paginationInfo);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value="homeAJAX.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String homeAJAX(CommandMap commandMap) {
		int currentPageNo = commandMap.getIntValue("pageNo", 1);
		int totalRecordCount = bookStorageService.getTotalCount();

		PaginationInfo paginationInfo = Util.newPaginationInfo(currentPageNo, totalRecordCount);
		
		int firstRecordIndex = paginationInfo.getFirstRecordIndex();
		int recordCountPerPage = paginationInfo.getRecordCountPerPage();
		
		List<Map<String, Object>> bookStorageViewDTOList = bookStorageService.getViewMapList(firstRecordIndex, recordCountPerPage);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("bookStorageViewDTOList", bookStorageViewDTOList);
		
		PaginationTagServlet tag = new PaginationTagServlet();
		tag.setJsFunction("linkPageAJAX");
		tag.setPaginationInfo(paginationInfo);
		tag.setType("text");
		tag.getHtml();
		jsonObj.put("pagination", tag.getHtml());
		
		return jsonObj.toString();
	}
	
	@PostMapping("getBook.do")
	public ModelAndView getBook(CommandMap commandMap) {
		ModelAndView mv = new ModelAndView("admin/bookStorage");

		String searchType = commandMap.getStrValue("searchType");
		String searchValue = commandMap.getStrValue("searchValue");
		
		if (searchType.equals("title")) {
			int totalRecordCount = bookStorageService.getTotalCount(searchValue); 

			PaginationInfo paginationInfo = Util.newPaginationInfo(1, totalRecordCount);
			
			List<BookStorageViewDTO> bookStorageViewDTOList = bookStorageService.getViewList(
					searchValue, paginationInfo.getFirstRecordIndex(), paginationInfo.getRecordCountPerPage());
			
			mv.addObject("bookStorageViewDTOList", bookStorageViewDTOList);
			mv.addObject("paginationInfo", paginationInfo);
		} else if (searchType.equals("book_no")) {
			int bookNo = Integer.parseInt(searchValue);
			
			BookStorageViewDTO bookStorageViewDTO = bookStorageService.getView(bookNo);
			List<BookStorageViewDTO> bookStorageViewDTOList = List.of(bookStorageViewDTO);
			
			PaginationInfo paginationInfo = Util.newPaginationInfo(1, 1);
			
			mv.addObject("bookStorageViewDTOList", bookStorageViewDTOList);
			mv.addObject("paginationInfo", paginationInfo);
		}
		
		mv.addObject("searchType", searchType);
		mv.addObject("searchValue", searchValue);
		
		return mv;
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value="getBookAJAX.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String getBookAJAX(CommandMap commandMap) {
		int bookNo = commandMap.getIntValue("bookNo");
		
		Map<String, Object> bookStorageViewDTO = bookStorageService.getViewMap(bookNo);
		List<Map<String, Object>> bookStorageViewDTOList = List.of(bookStorageViewDTO);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("bookStorageViewDTOList", bookStorageViewDTOList);
		
		return jsonObj.toString();
	}

	@PostMapping(value="registUnregisteredBook.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String registUnregisteredBook() {
		String resultMessage = "";
		
		List<Map<String, Object>> newBookStorageViewDTOList = bookStorageService.getUnregisteredViewMapList();
		if (newBookStorageViewDTOList.size() > 0) {
			int result = bookStorageService.insertBookList(newBookStorageViewDTOList);
			if (result > 0)
				resultMessage = "일괄 등록에 성공했습니다.";
			else 
				resultMessage = "일괄 등록에 실패했습니다.";
		} else {
			resultMessage = "등록할 데이터가 없습니다.";
		}

		return resultMessage;
	}
		
	@GetMapping("unregisteredBook.do")
	public ModelAndView unregisteredBook() {
		ModelAndView mv = new ModelAndView("admin/bookStorage");

		int currentPageNo = 1;
		int totalRecordCount = bookStorageService.getTotalUnregisteredCount();
		
		PaginationInfo paginationInfo = Util.newPaginationInfo(currentPageNo, totalRecordCount);

		int firstRecordIndex = paginationInfo.getFirstRecordIndex();
		int recordCountPerPage = paginationInfo.getRecordCountPerPage();
		
		List<Map<String, Object>> bookStorageViewDTOList = bookStorageService.getUnregisteredViewMapList(firstRecordIndex, recordCountPerPage);
		mv.addObject("bookStorageViewDTOList", bookStorageViewDTOList);
		mv.addObject("paginationInfo", paginationInfo);
		
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping(value="unregisteredBookAJAX.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String unregisteredBookAJAX(CommandMap commandMap) {
		int currentPageNo = commandMap.getIntValue("pageNo", 1);
		int totalRecordCount = bookStorageService.getTotalUnregisteredCount();

		PaginationInfo paginationInfo = Util.newPaginationInfo(currentPageNo, totalRecordCount);
		
		int firstRecordIndex = paginationInfo.getFirstRecordIndex();
		int recordCountPerPage = paginationInfo.getRecordCountPerPage();
		
		List<Map<String, Object>> bookStorageViewDTOList = bookStorageService.getUnregisteredViewMapList(firstRecordIndex, recordCountPerPage);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("bookStorageViewDTOList", bookStorageViewDTOList);
		
		PaginationTagServlet tag = new PaginationTagServlet();
		tag.setJsFunction("linkPageAJAX");
		tag.setPaginationInfo(paginationInfo);
		tag.setType("text");
		tag.getHtml();
		jsonObj.put("pagination", tag.getHtml());
		
		return jsonObj.toString();
	}
	
	@PostMapping(value="updateBook.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String updateBook(CommandMap commandMap) {
		int bookNo = commandMap.getIntValue("bookNo");
		int maxCount = commandMap.getIntValue("maxCount");
		
		BookStorageDTO dto = new BookStorageDTO();
		dto.setBook_no(bookNo);
		dto.setMax_count(maxCount);
		
		int result = bookStorageService.updateMaxCount(dto); 
		
		return String.valueOf(result);
	}
	

	@PostMapping(value="autoLoan.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String autoLoan(CommandMap commandMap) {
		String errorMessage = loanService.autoLoan(); 
		
		return errorMessage;
	}
	

	@PostMapping(value="autoReturn.do", produces="text/plain;charset=utf-8")
	@ResponseBody
	public String autoReturn(CommandMap commandMap) {
		String errorMessage = loanService.autoReturn(); 
		
		return errorMessage;
	}
}
