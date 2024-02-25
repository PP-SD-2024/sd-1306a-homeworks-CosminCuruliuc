<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Ștergeti un Student</title>
</head>
<body>
    <h2>Formular pentru stergerea unui student</h2>
    <form action="./delete-student" method="post">
        ID Student: <input type="number" name="id" />
        <br />
        <button type="submit">Sterge Student</button>
    </form>
</body>
</html>
