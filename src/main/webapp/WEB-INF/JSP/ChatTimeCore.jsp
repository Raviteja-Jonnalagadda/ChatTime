<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="ct" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Chat Time Dashboard</title>
<link href="<ct:url value='/resources/css/bashbord.css?v=<%= System.currentTimeMillis() %>' />" rel="stylesheet">
<link href="<ct:url value='/resources/css/bashboard_mobile_768.css?v=<%= System.currentTimeMillis() %>' />" rel="stylesheet">
<script>
    var contextPath = "<%= request.getContextPath() %>";
</script>
</head>
<body>
  <!-- Top Bar -->
  <div class="top-bar">
    <div>
<img src="<ct:url value='/resources/images/chat_App_logo.jpg' />" alt="Logo">
      </div>
      <div>
      <span class="app-title">Chat Time</span>
    </div>
<div class="datetime">
  <span id="date">Date: --</span>
  <span class="separator">|</span>
  <span id="time">Time: --</span>
</div>

  </div>
   <div class="ctap-search">
  <div class="ctap-srtxt">
    <input type="text" id="ctap_id_search" class="ctap-id-search" placeholder="Search ID">
  </div>
  <div class="ctap-btn">
    <button id="ctap_id_search_btn" class="ctap-id-search-btn">🔍</button>
  </div>
</div>
  <!-- Main Layout -->
  <div class="main-container">

    <!-- Sidebar -->
    <div class="sidebar">
      <ul id="menu"></ul>
    </div>

<iframe class="chat-area" id="ctap_main_frame" src="<%= request.getContextPath() %>/CTAP_WelcomePage" value="" title="Chat Area"></iframe>
  </div>

  <!-- Footer -->
  <div class="footer">
    <a href="#">Privacy Policy</a> | 
    <a href="#">Terms & Conditions</a>
  </div>

<script src="<ct:url value='/resources/js/jquery-3.7.1.min.js' />" ></script>
<script src="<ct:url value='/resources/js/bashboard.js?v=<%= System.currentTimeMillis() %>' />"></script>
<script src="<ct:url value='/resources/js/ChatApp_Utils.js?v=<%= System.currentTimeMillis() %>' />"></script>
</body>
</html>
