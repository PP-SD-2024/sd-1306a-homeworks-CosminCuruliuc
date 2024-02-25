import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import beans.StudentBean;
import java.io.File;
import java.io.IOException;

public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));

        XmlMapper xmlMapper = new XmlMapper();
        File file = new File("/home/student/IdeaProjects/sd-1306a-homeworks-CosminCuruliuc/SD_Laborator_1/student.xml");
        StudentBean student = xmlMapper.readValue(file, StudentBean.class);

        student.setNume(nume);
        student.setPrenume(prenume);
        student.setVarsta(varsta);
        xmlMapper.writeValue(file, student);

        response.sendRedirect("./read-student");
    }
}