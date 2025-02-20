function onclickSearchBtn() {
	var bookNo = $("input[name=bookNo]").val();
	
	$.ajax({
		url : "/cyber/admin/loan/searchBookAJAX.do",
		type : "POST",
		dataType : "json",
		data : { "bookNo" : bookNo },
		success : function(data) {
			if (data.errorMessage != "")
				alert(data.errorMessage);
			else
				rewriteSearchTbody(data.bookStorageViewDTO);
		},
		error : function(request, status, error) {
			alert("error : " + error);
		}
	});
}

function rewriteSearchTbody(bookStorageViewDTO) {
	$("#searchTbody").empty();

	var html = "";
	html += "<tr>";
	html += 	"<td>" + bookStorageViewDTO.book_no + "</td>";
	html += 	"<td>" + bookStorageViewDTO.title + "</td>";
	html += 	"<td>" + bookStorageViewDTO.author + "</td>";
	html += 	"<td>" + bookStorageViewDTO.isbn + "</td>";
	html += 	"<td>" + bookStorageViewDTO.max_count + "</td>";
	html += 	"<td>" + bookStorageViewDTO.loan_count + "</td>";
	html += 	"<td>" + bookStorageViewDTO.reserve_count + "</td>";
	html += "</tr>";

	$("#searchTbody").append(html);
}

function onclickLoanBtn() {
	var bookNo = $("input[name=bookNo]").val();
	var memberNo = $("input[name=memberNo]").val();
	
	$.ajax({
		url : "/cyber/admin/loan/loanAJAX.do",
		type : "POST",
		dataType : "json",
		data : { "bookNo" : bookNo, "memberNo" : memberNo },
		success : function(data) {
			if (data.errorMessage != "") {
				alert(data.errorMessage);
				return;
			}
			rewriteSearchTbody(data.bookStorageViewDTO);
			rewriteLoanTbody(data);
		},
		error : function(request, status, error) {
			alert("error : " + error);
		}
	});
}

function rewriteLoanTbody(data) {
	$("#loanTbody").empty();
	
	var html = "";
	var list = data.loanViewDTOList;
	for (var i = 0; i < list.length; i++) {
		var l = list[i];
		
		html += "<tr>";
		html += 	"<td>" + l.book_no + "</td>";
		html += 	"<td>" + l.title + "</td>";
		html += 	"<td>" + l.author + "</td>";
		html += 	"<td>" + l.return_date + "</td>";
		html += 	"<td>" + l.state + "</td>";
		if (l.state == 0)
			html += 	"<td><button type='button' onclick='onclickReturnBtn(" + l.book_no + ")'>반납실행</button></td>"
		html += "</tr>";
	}
	
	$("#loanTbody").append(html);
}

function onclickReserveBtn() {
	var bookNo = $("input[name=bookNo]").val();
	var memberNo = $("input[name=memberNo]").val();
	
	$.ajax({
		url : "/cyber/admin/loan/reserveAJAX.do",
		type : "POST",
		dataType : "json",
		data : { "bookNo" : bookNo, "memberNo" : memberNo },
		success : function(data) {
			if (data.errorMessage != "") {
				alert(data.errorMessage);
				return;
			}
			rewriteSearchTbody(data.bookStorageViewDTO);
			rewriteLoanTbody(data);
		},
		error : function(request, status, error) {
			alert("error : " + error);
		}
	});	
}

function onclickReturnBtn(no, bookNo) {
	var memberNo = $("input[name=memberNo]").val();
	
	$.ajax({
		url : "/cyber/admin/loan/returnAJAX.do",
		type : "POST",
		dataType : "json",
		data : { "no" : no, "bookNo" : bookNo, "memberNo" : memberNo },
		success : function(data) {
			if (data.errorMessage != "") {
				alert(data.errorMessage);
				return;
			}
			$("input[name=bookNo]").val(bookNo);
			rewriteSearchTbody(data.bookStorageViewDTO);
			rewriteLoanTbody(data);
			alert("반납되었습니다.");
		},
		error : function(request, status, error) {
			alert("error : " + error);
		}
	});	
}