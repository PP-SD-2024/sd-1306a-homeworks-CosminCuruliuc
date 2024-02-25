import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
    public static Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:/home/student/IdeaProjects/sd-1306a-homeworks-CosminCuruliuc/SD_Laborator_1/database.db");
    }
}
