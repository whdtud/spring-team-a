<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일반전자책</title>
<style>
body {
	margin: 0;
	padding: 0;
}

#wrap {
	width: 1000px;
	margin: 0 auto;
}

header {
	width: 1000px;
	height: 130px;
	background: #111;
}

aside {
	float: left;
	width: 240px;
	height: 600px;
	background: #222;
}

main {
	float: left;
	width: 760px;
	height: 600px;
	background: #444;
}

footer {
	clear: both;
	width: 1000px;
	height: 100px;
	background: #555;
}

table {
	width: 100%;
	border: 1px solid #444444;
}

th, td {
	border: 1px solid #444444;
}

td img {
	width: 100px
}
</style>
</head>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="./resources/js/data.js"></script>

<script>
$(document).ready(function() {
		//alert("정상작동");
		//emp();
		paging(1);
		$("#search").click(function() {
			var searchTitle = $('#searchTitle').val();
			$.ajax({
				url : "./ebookMain.do",
				type : "POST",
				cache : false,
				dataType : "json",
				data : {
					"title" : searchTitle
				},
				success : function(data) {
					//alert("정상 : pageNo : " + data.PAGE_NO);
					//alert("정상 : list : " + data.docs);
					emp(data);
				},
				error : function(requst, status, error) {
					alert("error : " + error);
				}
			});
		});
});
function paging(pageNo, search) {
	if(search!=null){
		$.ajax({
			url : "./ebookMain.do",
			type : "POST",
			cache : false,
			dataType : "json",
			data : {
				"pageNo" : pageNo,
				"title" : search
			},
			success : function(data) {
				emp(data);
			},
			error : function(requst, status, error) {
				alert("error : " + error);
			}
		});
	} else{
		$.ajax({
			url : "./ebookMain.do",
			type : "POST",
			cache : false,
			dataType : "json",
			data : {
				"pageNo" : pageNo
			},
			success : function(data) {
				emp(data);
			},
			error : function(requst, status, error) {
				alert("error : " + error);
			}
		});
	}
}
function emp(data) {
	$("#mainTable").empty();
	$("#paging").empty();
	var search = data.search;
	var pageNo = parseInt(data.PAGE_NO);
	var list = data.docs;
	var totalCount = parseInt(data.TOTAL_COUNT);
	var temp = "<h1>페이지 번호 : " + pageNo + "</h1>";
	temp += "<h2>전체 글 수  : " + totalCount + "</h2>";
	temp += "<table>";
	temp += "<tr>";
	temp += "<td>책</td><td>제목</td><td>출판사</td><td>저자</td><td>ISBN</td>";
	temp += "</tr>";
	for (var i = 0; i < list.length; i++) {
		temp += "<tr>";
		temp += "<td><img src=" + list[i].TITLE_URL+"></td>";
		temp += "<td>" + list[i].TITLE + "</td>";
		temp += "<td>" + list[i].PUBLISHER + "</td>";
		temp += "<td>" + list[i].AUTHOR + "</td>";
		temp += "<td>" + list[i].EA_ISBN + "</td>";
		temp += "</tr>";
	}
	temp += "</table>";
	$("#mainTable").append(temp);
	
	var totalPage = parseInt(totalCount / 10);
	if(totalCount % 10 != 0){
		totalPage += 1; //10으로 나눈 나머지의 값이 있을 때 +1
	}
	var startPage = parseInt(pageNo / 10) * 10 + 1 ; // 혹시나 여긴 나중에 수정
	
	if(pageNo % 10 == 0 ){
		startPage = pageNo - 9; 
	} 
	
	var endPage = startPage + 9; //여기도 나중에 수정 
	if(totalPage < endPage){
		endPage = totalPage;
	}
	var nextPage = pageNo + 1;
	var prevPage = pageNo - 1;
	// 출력
	var paging = "";
	
	// 첫 페이지 설정
	if(pageNo != 1){
		paging += "<button onclick=paging(1)>처음</button>";	
	}else{
		paging +="<button disabled='disabled')>처음</button>";	
	}
	
	// 이전 페이지 설정
	if(pageNo > 1){
		paging += "<button onclick=paging("+ prevPage + ",'"+search+"')>이전</button>";	
	}else{
		paging +="<button disabled='disabled')>이전</button>";	
	}
	
	//for출력
	for(var i = startPage; i <= endPage; i++){
		if(pageNo != i){
			paging += "<button onclick=paging("+i+",'"+search+"')>" + i + "</button>";			
		}else{
			paging += "<b>" + i + "</b>";						
		}
	}
	
	// 이후 페이지 설정
	if(pageNo < totalPage){
		paging += "<button onclick=paging(" + nextPage + ",'"+search+"')>다음</button>"		
	}else{
		paging += "<button disabled='disabled')>다음</button>"			
	}
	
	if( pageNo < totalPage){
		paging += "<button onclick=paging(" + (totalPage) + ",'"+search+"')>마지막</button>"		
	}else{
		paging += "<button disabled='disabled')>마지막</button>"			
	}
	
	
	$("#paging").append(paging);
}

</script>
<body>
	<div id="wrap">
		<header>
			<c:import url="/WEB-INF/views/component/headerInner.jsp" />
		</header>
		<aside>
			<c:import url="/WEB-INF/views/component/lnbNav.jsp" />
		</aside>
		<main>
			<div>
				검색어<input id="searchTitle" style="margin-left: 20px; width: 400px;">
				<button id="search">검색</button>
			</div>
			<div id="mainTable">
				<%-- 검색 결과 : ${totalCount }건<br>
		<c:forEach var="l" items="${searchList }">
			<img alt="${l.TITLE }" src="${l.TITLE_URL }" style="width: 80px">
			제목 : ${l.TITLE }<br>
			출판사 : ${l.PUBLISHER }<br>
			저자 : ${l.AUTHOR }<br>
			ISBN : ${l.EA_ISBN }	
			<hr>		
		</c:forEach>	 --%>
			</div>
			<div id="paging"></div>
		</main>
		<footer>footer</footer>
	</div>
</body>
</html>