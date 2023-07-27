package rest.connection;

import java.sql.*;

public class ConnectionRest {

	Connection conn = null;
	public static Connection conDB() {
    	
    	try {
    		//konum - username - mysql
    		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/restaurant", "root", "mysql");
    		return con;
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
			return null;
		}
    }
	
}
