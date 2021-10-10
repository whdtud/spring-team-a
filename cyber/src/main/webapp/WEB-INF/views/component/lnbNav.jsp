<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style type="text/css">
#lnbNav {
	background-color: #e3f0ff;
}

#lnb {
	list-style: none;
	margin: 0;
	padding: 0;
	background: #e3f0ff;
}

#lnbWrap {
	background-color: white;
}

#lnbTitle {
	background-color: #e3f0ff;
	margin: 0;
}
</style>
<script>
	$(document).ready(function() {
		drawLnb();
	});

	function drawLnb() {
		var html = "";
		var map = getNaviMap();
		for ( var key in map) {
			html += "<li><a href='" + key + "'>" + map[key] + "</a></li>";
		}
		$("#lnb").append(html);
	}

	function getNaviMap() {
		var pathname = window.location.pathname;
		if (pathname.indexOf("/cyber/member/") != -1) {
			return getMemberNaviMap();
		} else if (pathname.indexOf("/cyber/myPage/") != -1) {
			return getMyPageNaviMap();
		}
		return getEbookNaviMap();
	}

	function getMemberNaviMap() {
		var map = {
			"/cyber/member/memberLogin.do" : "&nbsp;로그인",
			"/cyber/member/memberJoinRegist.do" : "&nbsp;회원가입",
			"/cyber/member/memberFindId.do" : "&nbsp;아이디찾기",
			"/cyber/member/memberFindPw.do" : "&nbsp;비밀번호찾기"
		};
		return map;
	}

	function getEbookNaviMap() {
		var map = {
			"/cyber/ebook/ebookMain.do" : "일반전자책",
			"/cyber/ebook/ebookChild.do" : "어린이전자책",
			"/cyber/ebook/ebookSearchDetail.do" : "전자책 상세검색",
			"/cyber/bbs/listNotice.do" : "공지사항"
		};
		return map;
	}

	function getMyPageNaviMap() {
		var map = {
			"/cyber/myPage/myPage.do" : "서비스 이용현황",
			"/cyber/myPage/ebookLoanList.do" : "전자책",
			"/cyber/myPage/myBbsPostList.do" : "묻고답하기",
			"/cyber/myPage/pwdCheck.do" : "회원정보수정"
		};
		return map;
	}
</script>
<div id="lnbNav">
	<div id="lnbTitle">
		<h2>&nbsp;Menu</h2>
	</div>
	<div id="lnbWrap">
		<ul id="lnb" style="font-size: 18px;">
		</ul>
	</div>
</div>