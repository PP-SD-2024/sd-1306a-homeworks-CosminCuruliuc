import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProcessStudentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Citirea parametrilor din cerere
        String nume = request.getParameter("nume");
        String prenume = request.getParameter("prenume");
        int varsta = Integer.parseInt(request.getParameter("varsta"));

        // Inserarea în baza de date
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO students (nume, prenume, varsta) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nume);
                pstmt.setString(2, prenume);
                pstmt.setInt(3, varsta);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirecționarea la o altă pagină pentru afișarea rezultatului
        response.sendRedirect("./read-student");
    }
}
