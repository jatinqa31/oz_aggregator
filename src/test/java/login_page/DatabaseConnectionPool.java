package login_page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.annotations.Test;

public class DatabaseConnectionPool {

    public static Connection connection;
    
    static {
	 	String url = "jdbc:mysql://localhost:3306/lexerpos_db";
	 	String username = "root";
	 	String password = "";
		try {
			connection = DriverManager.getConnection(url, username, password);	
			System.out.println("Db Connected!2");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
//    @Test(priority=0,enabled=true,invocationCount = Integer.MAX_VALUE, threadPoolSize = 1)
    public static Connection getConnected() {
        return connection;
    }

}

//POINTS TO BE NOTED EARLIER I HAVE MADE CONNECTION EVRY TIME TO CALL DB FROM EVERY FUNCTION I USED BUT
//NOW I HAVE USED A SIGNLE STATIC CONNECTION TO CONNECT WITH DATABASE...

//BUT BUT BUT IT DOESNOT MAKE SIGNIFICANT DIFFERENCE TRULLY SPEAKING......

//SPEED DIFF IS MADE WHEN I REDUCE TIME OF EXPLICIT WAITS IN SELENIUM WHICH MAKES EXECUTION FAST