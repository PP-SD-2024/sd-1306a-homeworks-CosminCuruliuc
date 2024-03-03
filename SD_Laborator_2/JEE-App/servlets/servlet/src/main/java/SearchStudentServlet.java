import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SearchStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        String queryString = "SELECT s FROM StudentEntity s WHERE ";
        boolean hasName = !nume.isEmpty();
        boolean hasSurname = !prenume.isEmpty();

        if (hasName) {
            queryString += "s.nume = :nume";
            if (hasSurname) queryString += " AND ";
        }
        if (hasSurname) {
            queryString += "s.prenume = :prenume";
        }

        TypedQuery<StudentEntity> query = em.createQuery(queryString, StudentEntity.class);
        if (hasName) query.setParameter("nume", nume);
        if (hasSurname) query.setParameter("prenume", prenume);

        List<StudentEntity> results = query.getResultList();

        response.setContentType("text/html;charset=UTF-8");
        try (java.io.PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Rezultate Căutare</title></head><body>");
            if (results.isEmpty()) {
                out.println("<h2>Nu s-au găsit studenți conform criteriilor de căutare.</h2>");
                out.println("<a href='./index.jsp'>Înapoi la pagina principală</a>");
            } else {
                out.println("<h2>Formular de actualizare</h2>");
                for (StudentEntity student : results) {
                    out.println("<div>");
                    out.println("<h3>Date actuale ale studentului:</h3>");
                    out.println("Nume: " + student.getNume() + "<br/>Prenume: " + student.getPrenume() + "<br/>Varsta: " + student.getVarsta());
                    out.println("<h3>Introduceți datele noi:</h3>");
                    out.println("<form action='./update-student' method='post'>");
                    out.println("<input type='hidden' name='studentId' value='" + student.getId() + "' />");
                    out.println("Nume: <input type='text' name='nume' value='" + student.getNume() + "'/><br/>");
                    out.println("Prenume: <input type='text' name='prenume' value='" + student.getPrenume() + "'/><br/>");
                    out.println("Varsta: <input type='number' name='varsta' value='" + student.getVarsta() + "'/><br/>");
                    out.println("<input type='submit' value='Actualizează'/>");
                    out.println("</form>");
                    out.println("</div><hr/>");
                }
                out.println("<a href='./index.jsp'>Înapoi la pagina principală</a>");
            }
            out.println("</body></html>");
        } finally {
            em.close();
            factory.close();
        }
    }
}
