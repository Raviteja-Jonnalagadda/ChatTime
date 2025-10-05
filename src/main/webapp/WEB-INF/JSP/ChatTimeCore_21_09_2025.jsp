<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Chat Dashboard</title>
    <!-- Link CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat_core.css">

</head>
<body>

  <!-- Top Bar -->
  <div class="top-bar">
    <div style="display:flex; align-items:center;">
      <img src="${pageContext.request.contextPath}/images/chat_App_logo.jpg" alt="Logo">
      <span class="app-title">Chat Time</span>
    </div>
    <div>
      <span id="date">Date: --</span> | 
      <span id="time">Time: --</span>
    </div>
  </div>

  <!-- Main Layout -->
  <div class="main-container">
    <!-- Sidebar -->
    <div class="sidebar">
      <ul>
        <li>Chats</li>
        <li>Groups</li>
        <li>Important</li>
        <li>Settings</li>
      </ul>
    </div>

    <!-- Chat Area -->
    <div class="chat-area">
      <div class="chat-header">Ravi Teja</div>
      <div class="messages" id="messages">
        <div class="message">Hello! How are you?</div>
        <div class="message sent">I am good, thanks! What about you?</div>
        <div class="message">Doing great 🚀</div>
      </div>
      <div class="chat-input">
        <input type="text" id="msgInput" placeholder="Type a message...">
        <button id="sendBtn">Send</button>
      </div>
    </div>
  </div>

  <!-- Footer -->
  <div class="footer">
    <a href="#">Privacy Policy</a> | 
    <a href="#">Terms & Conditions</a>
  </div>
  
   <!-- jQuery should be loaded first -->
    <script src="${pageContext.request.contextPath}/js/min/jquery-3.7.1.min.js"></script>

    <!-- Core JS -->
    <script src="${pageContext.request.contextPath}/js/chat_core.js"></script>

</body>
</html>
