<html>
    <body>
        <h2>Baza de date pentru studentii facultatii de AC</h2>
        <br />
        <p>
            <a href="./add-student.jsp">Adaugare student</a>
        </p>
        <p>
            <a href="./read-student">Vizualizare studenti</a>
        </p>
        <p>
            <a href="./update-student.jsp">Actualizare student</a>
        </p>
        <p>
            <a href="./delete-student.jsp">Stergere student</a>
        </p>
        <p>
            <a href="./exportDatabase">Exportati baza de data ca JSON</a>
        </p>
        <form action="./search-student" method="get">
            Cautare dupa nume: <input type="text" name="name" />
            <input type="submit" value="Cauta" />
        </form>
    </body>
</html>
