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

public class SearchStudentForDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        TypedQuery<StudentEntity> query = em.createQuery("SELECT s FROM StudentEntity s WHERE s.nume LIKE :nume AND s.prenume LIKE :prenume", StudentEntity.class);
        query.setParameter("nume", "%" + nume + "%");
        query.setParameter("prenume", "%" + prenume + "%");

        List<StudentEntity> results = query.getResultList();

        response.setContentType("text/html;charset=UTF-8");
        try (java.io.PrintWriter out = response.getWriter()) {
            out.println("<html><head><title>Rezultate Căutare pentru Ștergere</title></head><body>");
            if (results.isEmpty()) {
                out.println("<h2>Nu s-au găsit studenți conform criteriilor de căutare.</h2>");
            } else {
                for (StudentEntity student : results) {
                    out.println("<p>" + student.getNume() + " " + student.getPrenume() +
                            " <a href='./delete-student?studentId=" + student.getId() + "'>Șterge</a></p>");
                }
            }
            out.println("<a href='./index.jsp'>Înapoi la pagina principală</a>");
            out.println("</body></html>");
        } finally {
            em.close();
            factory.close();
        }
    }
}
