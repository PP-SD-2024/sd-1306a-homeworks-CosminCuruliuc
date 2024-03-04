<%@ page contentType="text/html; charset=UTF-8" %>
<html>
	<head>
		<title>Meniu principal</title>
		<meta charset="utf-8" />
	</head>
	<body>
		<h1>Meniu principal</h1>
		<hr />
		<h3>Gestiune cont bancar</h3>
		<form action="./process-bank-operation" method="post">
			<fieldset label="operatiuni">
				<legend>Alegeti operatiunea dorita:</legend>
				<select name="operation">
					<option value="deposit">Depunere numerar</option>
					<option value="withdraw">Retragere numerar</option>
					<option value="balance">Interogare sold</option>
				</select>
				<br />
				<br />
				Introduceti suma: <input type="number" name="amount" />
				<br />
				<br />
				<button type="submit">Efectuare</button>
			</fieldset>
		</form>
		<hr />
		<h3>Baza de date cu studenti</h3>
		<a href="./formular.jsp">Adaugare student</a>
		<br />
		<a href="./fetch-student-list">Afisare lista studenti</a>
		<br />
        <a href="./search-student-for-update.jsp">Actualizare student</a>
        <br />
        <a href="./search-student-for-delete.jsp">Stergere student</a>

        <br />
        <h3>Monitorizare Vârstă Studenți</h3>
        <form action="monitor" method="get">
           <fieldset>
           <legend>Setați limitele de vârstă pentru monitorizare:</legend>
           Limita inferioară (a): <input type="number" name="a" required>
           <br /><br />
           Limita superioară (b): <input type="number" name="b" required>
           <br /><br />
           <button type="submit">Inițiază Monitorizarea</button>
           </fieldset>
        </form>

                <script>
                    function checkForErrors() {
                        fetch('checkError')
                            .then(response => response.text())
                            .then(text => {
                                if (text) {
                                    alert("Eroare detectată: " + text);
                                    // Opțional: Redirecționează către o pagină de eroare detaliată
                                    // window.location.href = "/errorPage.jsp";
                                }
                            })
                            .catch(error => console.error('Eroare la verificarea erorilor:', error));
                    }

                    setInterval(checkForErrors, 5000); // Verifică la fiecare 5 secunde
                </script>
	</body>
</html>