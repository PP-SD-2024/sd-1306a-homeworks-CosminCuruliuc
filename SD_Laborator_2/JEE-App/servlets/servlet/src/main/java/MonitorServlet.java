import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MonitorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int lowerBound = Integer.parseInt(req.getParameter("a"));
        int upperBound = Integer.parseInt(req.getParameter("b"));

        ServletContext context = getServletContext();
        new Thread(new DatabaseMonitor(lowerBound, upperBound, context)).start();

        resp.getWriter().println("Monitorizarea a fost initiata.");
    }
}
