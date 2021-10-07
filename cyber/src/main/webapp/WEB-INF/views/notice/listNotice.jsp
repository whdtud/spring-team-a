<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<script type="text/javascript">
function linkPage(pageNo) {
	location.href="./listNotice.do?pageNo=" + pageNo;
}
</script>
<style>
	body { margin: 0; padding: 0; }
	#wrap { width: 1000px; margin: 0 auto; }
	header { width: 1000px; height: 130px; background: #111; }
	aside { float: left; width: 240px; height: 600px; background: #333; }
	main { float: left; width: 760px; height: 600px; background: #444; }
	footer	{ clear: both; width: 1000px; height: 100px; background: #555; }
	
	#pageSearch { margin-left: 10px; }
	#pageCategory { margin-left: 10px; }
	#noticeBoard { margin: 10px; width: 600px; height: 300px; }
	table { border-collapse: collapse; border-spacing: 0; background-color: white; }
	th { background-color: gray; }
	tr { border-bottom: 1px solid gray; }
	td { text-align: center; }
	#title { text-align: left; }
	a { text-decoration: none; color: black; }
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src='<c:url value="/resources/js/notice/listNotice.js"/>'></script>
</head>

<body>
	<div id="wrap">
		<header>
			<c:import url="/WEB-INF/views/component/headerInner.jsp" />
		</header>
		<aside>
			<c:import url="/WEB-INF/views/component/lnbNav.jsp" />
		</aside>
		
		<main>
			<div id="naviandtitle">
				<div id="navi">
					<a href="./index.do">Home</a>>공지사항
				</div> <!-- end of navi -->
			<h1>공지사항</h1>
			</div> <!-- end of naviandtitle -->
			
			<!-- 검색 기능 -->
			<form action="pageSearch" id="pageSearch" method="get">
			<select name="searchCondition" id="searchCondition" title="검색방법 선택">
				<option value="title">제목</option>
				<option value="content">내용</option>
			</select>
			<input type="text" name="searchKeyword" id="searchKeyword" title="검색어 입력" placeholder="검색어를 입력해주세요.">
			<button type="submit" id="searchBtn">검색</button>
			</form>
			
			<!-- 카테고리 기능 -->
			<form action="pageCategory" id="pageCategory" method="get">
				<select name="categoryCondition" id="categoryCondition" title="카테고리 선택">
					<option value="">카테고리</option>
					<option value="home">홈페이지</option>
					<option value="ebook">전자책</option>
					<option value="freeboard">소통마당</option>
					<option value="mylibrary">나만의 도서관</option>
				</select>
				<button type="submit" id="categoryBtn">확인</button>
			</form>
			
			<!-- 공지사항 list -->
			<div id="noticeBoard">
				<table>
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>등록일</th>
							<th>조회수</th>
							<th>작성자</th>
						</tr>
					</thead>
					<tbody id="nbody">
						<c:forEach items="${list }" var="l">
							<tr>
								<td>${l.notice_no }</td>
								<td><a href="./noticeDetail.do?notice_no=${l.notice_no }">${l.title }</a></td>
								<td>${l.date }</td>
								<td>${l.count }</td>
								<td>${l.id }(${l.name })</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div> <!-- end of noticeBoard -->
				
			<!-- 페이징-->
			<div id="pagination">
				<ui:pagination paginationInfo="${paginationInfo }" type="text" jsFunction="linkPage"/>
			</div> <!-- end of paging -->
			
			<a href="./noticeWrite.do">글쓰기</a>
						
		</main>
		
		<footer>footer</footer>
	</div> <!-- end of wrap -->
</body>
</html>