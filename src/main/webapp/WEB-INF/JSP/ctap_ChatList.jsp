<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="ct" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link href="<ct:url value='/resources/css/ChatList.css?v=<%= System.currentTimeMillis() %>' />" rel="stylesheet">
<script src="<ct:url value='/resources/js/jquery-3.7.1.min.js' />"></script>
<script src="<ct:url value='/resources/js/ChatList.js?v=<%= System.currentTimeMillis() %>' />"></script>
<script src="<ct:url value='/resources/js/ChatApp_Utils.js?v=<%= System.currentTimeMillis() %>' />"></script>
</head>
<body>
	<table id="ctap_main_list" class="ctap-ct-list-table">
	</table>
</body>
</html>