<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Adăugați un Student</title>
</head>
<body>
    <h2>Formular pentru adăugarea unui student</h2>
    <form action="./process-student" method="post">
        Nume: <input type="text" name="nume" />
        <br />
        Prenume: <input type="text" name="prenume" />
        <br />
        Varsta: <input type="number" name="varsta" />
        <br />
        <button type="submit">Adaugă Student</button>
    </form>
</body>
</html>
