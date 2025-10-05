<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            text-align: center;
            padding-top: 100px;
        }
        h1 {
            color: #333;
        }
    </style>
</head>
<body>
    <%
        // Server-side Java code
        String userName = request.getParameter("user");
        if (userName == null || userName.trim().isEmpty()) {
            userName = "Guest";
        }
    %>

    <h1>Welcome, <%= userName %>!</h1>
    <p>This is your Faild JSP welcome page.</p>
</body>
</html>
