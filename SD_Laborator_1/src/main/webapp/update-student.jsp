<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Actualizati un Student</title>
</head>
<body>
    <h2>Formular pentru actualizarea datelor unui student</h2>
    <form action="./update-student" method="post">
        ID Student: <input type="number" name="id" />
        <br />
        Nume Nou: <input type="text" name="nume" />
        <br />
        Prenume Nou: <input type="text" name="prenume" />
        <br />
        Varsta Nouă: <input type="number" name="varsta" />
        <br />
        <button type="submit">Actualizeaza Student</button>
    </form>
</body>
</html>
