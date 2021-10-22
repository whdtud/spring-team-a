package com.teama.board.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.teama.board.service.BoardServiceImpl;
import com.teama.common.CommandMap;
import com.teama.util.FileSave;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
@RequestMapping("/bbs")
public class BoardController {

	@Resource(name="boardService")
	private BoardServiceImpl boardService;
	
	@Autowired
	private FileSave fileSave;
	
	@Autowired
	private ServletContext servletContext;
	
	//게시글 불러오기
	@RequestMapping("/listBoard.do")
	public ModelAndView boardList(CommandMap map) {
		ModelAndView mv = new ModelAndView("board/listBoard");
		
		//검색값을 jsp로 넘기기
		if(map.containsKey("searchKeyword")) {
			mv.addObject("searchKeyword", map.get("searchKeyword"));
			mv.addObject("searchCondition", map.get("searchCondition"));
		}
		
		setPagination(map, mv);
		
		return mv;
	}
	
	//게시글 상세보기
	@RequestMapping("/boardDetail.do")
	public ModelAndView detail(CommandMap map) {
		ModelAndView mv = new ModelAndView("board/boardDetail");
		
		//댓글 페이징
		int commentPageNo = 1; //첫 페이지 보여주세요.
		if(map.containsKey("commentPageNo")) {
			commentPageNo = Integer.parseInt( String.valueOf (map.get("commentPageNo")) );
		}
		
		int commentListScale = 5;
		int commentPageScale = 5;
		
		//totalCount
		int commentTotalCount = boardService.commentTotalCount(map.getMap());
		System.out.println("댓글이" + commentTotalCount + "개가 있습니다.");
		
		//전자정부 페이징 불러오기
		PaginationInfo commentPaginationInfo = new PaginationInfo(); //이 객체에 값을 넣어주어야 한다.
		commentPaginationInfo.setCurrentPageNo(commentPageNo);
		commentPaginationInfo.setRecordCountPerPage(commentListScale);
		commentPaginationInfo.setPageSize(commentPageScale);
		commentPaginationInfo.setTotalRecordCount(commentTotalCount);
		
		//계산하기
		int commentStartPage = commentPaginationInfo.getFirstRecordIndex(); //시작페이지
		int commentLastPage = commentPaginationInfo.getRecordCountPerPage(); //마지막페이지
		
		//DB로 보내기 위해서 map에 담아주세요.
		map.put("commentStartPage", commentStartPage);
		map.put("commentLastPage", commentLastPage);
		
		//질의
		//조회수
		boardService.count(map.getMap());
		//상세보기
		Map<String, Object> detail = boardService.detail(map.getMap());
		//댓글 불러오기
		List<Map<String, Object>> commentList = boardService.boardCommentList(map.getMap());
		//이전글 다음글
		Map<String, Object> preNextPage = boardService.preNextPage(map.getMap());

		mv.addObject("detail", detail); //앞엔 호출이름, 뒤는 값
		mv.addObject("commentList", commentList);
		mv.addObject("commentPaginationInfo", commentPaginationInfo);
		mv.addObject("commentPageNo", commentPageNo);
		mv.addObject("commentTotalCount", commentTotalCount);
		mv.addObject("preNextPage", preNextPage);
		
		return mv;
	}
	
	//게시글 상세보기 -> 글쓰기 페이지로 넘기기
	@GetMapping("/boardWrite.do")
	public String write() {
		return "board/boardWrite";
	}
	
	//값 받아서 글쓰기
	@PostMapping("/boardWrite.do")
	public ModelAndView postWrite(CommandMap map, HttpServletRequest request, MultipartFile file) throws IOException {
		ModelAndView mv = new ModelAndView("board/listBoard");

		HttpSession session = request.getSession();
		map.put("id", session.getAttribute("id"));
		map.put("originalfile", file.getOriginalFilename());
		
		String realPath = servletContext.getRealPath("resources/upfile2/");
		String upfile = fileSave.save2(realPath, file);
		map.put("file", upfile);
		
		System.out.println("파일 위치는? = " + realPath);
		int result = boardService.write(map.getMap());
		System.out.println("boardWrite = " + result);
		
		setPagination(map, mv);
		
		return mv;
	}
	
	//게시글 삭제
	@RequestMapping("/boardDelete.do")
	public String delete(CommandMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		map.put("id", session.getAttribute("id"));
		int result = boardService.delete(map.getMap());
		System.out.println("boardDelete = " + result);

		return "redirect:listBoard.do";
	}
	
	//게시글 상세보기 -> 수정하기 페이지로 값 가지고 넘기기
	@GetMapping("/boardUpdate.do")
	public ModelAndView update(CommandMap map) {
		ModelAndView mv = new ModelAndView("board/boardUpdate");
		Map<String, Object> detail = boardService.detail(map.getMap());
		mv.addObject("detail", detail);
		return mv;
	}
	
	//값 받아서 수정하기
	@PostMapping("/boardUpdate.do")
	public String postUpdate(CommandMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		map.put("id", session.getAttribute("id"));
		int result = boardService.update(map.getMap());
		System.out.println("boardUpdate = " + result);
		return "redirect:boardDetail.do?no="+map.get("no");
	}
	
	//페이징
	private void setPagination(CommandMap map, ModelAndView mv) {
		//페이지 번호가 오는지 확인하기
		int pageNo = 1; //첫 페이지 보여주세요.
		if(map.containsKey("pageNo")) {
			pageNo = Integer.parseInt( String.valueOf (map.get("pageNo")) );
		}
		int listScale = 10;
		int pageScale = 10;
		
		//totalCount
		int totalCount = boardService.totalCount(map.getMap());
		System.out.println(totalCount + "개가 있습니다.");
		
		//전자정부 페이징 불러오기
		PaginationInfo paginationInfo = new PaginationInfo(); //이 객체에 값을 넣어주어야 한다.
		paginationInfo.setCurrentPageNo(pageNo);
		paginationInfo.setRecordCountPerPage(listScale);
		paginationInfo.setPageSize(pageScale);
		paginationInfo.setTotalRecordCount(totalCount);
		
		//계산하기
		int startPage = paginationInfo.getFirstRecordIndex(); //시작페이지
		int lastPage = paginationInfo.getRecordCountPerPage(); //마지막페이지
		
		//DB로 보내기 위해서 map에 담아주세요.
		map.put("startPage", startPage);
		map.put("lastPage", lastPage);
		
		//질의
		List<Map<String, Object>> list = boardService.boardList(map.getMap());

		mv.addObject("list", list); //앞엔 호출이름, 뒤는 값
		mv.addObject("paginationInfo", paginationInfo);
		mv.addObject("pageNo", pageNo);
		mv.addObject("totalCount", totalCount);
	}
	
	//게시물 댓글쓰기
	@PostMapping("/commentWrite.do")
	public String postCommentWrite(CommandMap map, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		map.put("id", session.getAttribute("id"));
		
		int result = boardService.commentWrite(map.getMap());
		System.out.println("commentWrite = " + result);
		
		return "redirect:boardDetail.do?no="+map.get("no");
	}
	
	//게시물 댓글삭제
	@RequestMapping("/commentDelete.do")
	public String commentDelete(CommandMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		map.put("id", session.getAttribute("id"));
		int result = boardService.commentDelete(map.getMap());
		System.out.println("commentDelete = " + result);

		return "redirect:boardDetail.do?no="+map.get("no");
	}
	
	//게시물 댓글수정
	@PostMapping("/commentUpdate.do")
	public String postCommentUpdate(CommandMap map, HttpServletRequest request) {
		HttpSession session = request.getSession();
		map.put("id", session.getAttribute("id"));
		int result = boardService.commentUpdate(map.getMap());
		System.out.println("commentUpdate = " + result);
		
		return "redirect:boardDetail.do?no="+map.get("no");
	}
	
}
