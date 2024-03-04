import ejb.StudentEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import java.util.List;

public class DatabaseMonitor implements Runnable {
    private int lowerBound, upperBound;
    private ServletContext context;

    public DatabaseMonitor(int lowerBound, int upperBound, ServletContext context) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.context = context;
    }

    @Override
    public void run() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bazaDeDateSQLite");
        EntityManager em = emf.createEntityManager();

        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<StudentEntity> students = ((EntityManager) em).createQuery("SELECT s FROM StudentEntity s WHERE s.varsta < :lowerBound OR s.varsta > :upperBound", StudentEntity.class)
                        .setParameter("lowerBound", lowerBound)
                        .setParameter("upperBound", upperBound)
                        .getResultList();

                if (!students.isEmpty()) {
                    context.setAttribute("error", "Varsta unui sau mai multi studenti este in afara intervalului specificat.");
                } else {
                    context.removeAttribute("error");
                }

                Thread.sleep(1000); // Verifică la fiecare secundă
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            em.close();
            emf.close();
        }
    }
}

