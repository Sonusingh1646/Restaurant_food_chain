
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
 
public class Test {
    public static void main(String[] args) {
        Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("s");
            connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/Restaurant","sonukumar","Vnit@12345");
             
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO user (id,name,address,number) "
                                + "VALUES (1,'Lokesh','Gupta',55445)");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stmt.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}