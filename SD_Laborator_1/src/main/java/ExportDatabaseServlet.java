import beans.StudentBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ExportDatabaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<StudentBean> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                StudentBean student = new StudentBean();
                student.setNume(rs.getString("nume"));
                student.setPrenume(rs.getString("prenume"));
                student.setVarsta(rs.getInt("varsta"));
                students.add(student);
            }

            // Convertim lista de studenti in JSON
            ObjectMapper mapper = new ObjectMapper();
            String studentsJson = mapper.writeValueAsString(students);

            // Setam atributul pentru a fi folosit in JSP
            request.setAttribute("studentsJson", studentsJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirectionam către pagina JSP de confirmare
        request.getRequestDispatcher("/confirm-export.jsp").forward(request, response);
    }
}
