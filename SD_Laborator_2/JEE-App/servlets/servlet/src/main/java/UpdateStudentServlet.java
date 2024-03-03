import ejb.StudentEntity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Se preiau parametrii din cererea de tip POST
        int studentId = Integer.parseInt(request.getParameter("studentId"));
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = factory.createEntityManager();

        // Se începe o tranzacție pentru a actualiza entitatea
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        // Se caută studentul după ID și se actualizează
        StudentEntity student = em.find(StudentEntity.class, studentId);
        if (student != null) {
            student.setNume(nume);
            student.setPrenume(prenume);
            student.setVarsta(varsta);
            em.merge(student); // Actualizarea entității
        }

        transaction.commit();

        // Închiderea EntityManager și redirecționarea către lista de studenți
        em.close();
        factory.close();

        // Redirectiuonare catre pagina principala
        response.setContentType("text/html");
        response.getWriter().println("Datele au fost actualizate in baza de date." +
                "<br /><br /><a href='./'>Inapoi la meniul principal</a>");
    }
}

