import beans.StudentBean;
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

public class SearchStudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        List<StudentBean> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE nume LIKE ? OR prenume LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            pstmt.setString(2, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StudentBean student = new StudentBean();
                student.setNume(rs.getString("nume"));
                student.setPrenume(rs.getString("prenume"));
                student.setVarsta(rs.getInt("varsta"));
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("students", students);
        request.getRequestDispatcher("/view-students.jsp").forward(request, response);
    }
}

