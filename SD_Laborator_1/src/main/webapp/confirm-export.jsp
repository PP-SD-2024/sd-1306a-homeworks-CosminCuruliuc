<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Export Baza de Date ca JSON</title>
</head>
<body>
    <h2>Exportul a fost realizat cu succes!</h2>
    <p>Datele exportate ca JSON:</p>
    <pre><%= request.getAttribute("studentsJson") %></pre>
</body>
</html>
