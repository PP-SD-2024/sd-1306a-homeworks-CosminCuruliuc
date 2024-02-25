import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File file = new File("/home/student/IdeaProjects/sd-1306a-homeworks-CosminCuruliuc/SD_Laborator_1/student.xml");

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Fișierul a fost șters.");
            } else {
                System.out.println("Nu s-a putut șterge fișierul.");
            }
        }

        response.sendRedirect("./"); // Redirecționăm utilizatorul înapoi la pagina principală.
    }
}

