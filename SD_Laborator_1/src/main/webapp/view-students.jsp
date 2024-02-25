<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="beans.StudentBean"%>
<!DOCTYPE html>
<html>
<head>
    <title>Vizualizati Studentii</title>
</head>
<body>
    <h2>Lista Studentilor</h2>
    <%
        List<StudentBean> students = (List<StudentBean>) request.getAttribute("students");
        if (students != null && !students.isEmpty()) {
            out.println("<ul>");
            for (StudentBean student : students) {
                out.println("<li>" + student.getNume() + " " + student.getPrenume() + " - " + student.getVarsta() + " ani</li>");
            }
            out.println("</ul>");
        } else {
            out.println("<p>Nu exista studenti inregistrati.</p>");
        }
    %>
</body>
</html>
