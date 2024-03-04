import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorCheckingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String error = (String) context.getAttribute("error");

        if (error != null) {
            resp.setContentType("text/plain");
            resp.getWriter().write(error);
        } else {
            resp.getWriter().write(""); // Nici o eroare detectată
        }
    }
}

