import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));
        int id = Integer.parseInt(request.getParameter("id")); // Presupunem că trimitem și un ID

        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "UPDATE students SET nume = ?, prenume = ?, varsta = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nume);
                pstmt.setString(2, prenume);
                pstmt.setInt(3, varsta);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("view-students.jsp");
    }
}