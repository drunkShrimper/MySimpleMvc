<%@ page import="com.scau.myframework.mvc.test.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>show</title>
</head>
<body>
<%
    String password = (String)request.getAttribute("pwd");
    out.print(password);

    User user = (User)request.getAttribute("user");
    out.print("</br>" + user.getName());
%>



</body>
</html>