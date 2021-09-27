<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Index</title>
	<style>
		body { margin: 0; padding: 0; }
		#wrap { width: 1000px; margin: 0 auto; }
		header { width: 1000px; height: 130px; background: #111; }
		main { width: 1000px; height: 600px; background: #444; }
		footer	{ clear: both; width: 1000px; height: 100px; background: #555; }
	</style>
</head>
<body>
	<div id="wrap">
		<header>
			<c:import url="/WEB-INF/views/component/headerInner.jsp" />
		</header>
		<main>
			<table>
				<tr>
					<th>번호</th>
					<th>제목</th>
					<th>날짜</th>
					<th>조회수</th>
					<th>글쓴이</th>
				</tr>
				<c:forEach items="${list }" var="l">
					<tr>
						<td>${l.no }</td>
						<td>${l.title }</td>
						<td>${l.content }</td>
						<td>${l.date }</td>
						<td>${l.count }</td>
					</tr>
				</c:forEach>
			</table>
		</main>
		<footer>footer</footer>
	</div>
</body>
</html>