package rest;

import java.sql.*;

public class JdbcDao {
	//login panelinin baglant� s�n�f�
	//veri taban�
	private static final String DATABASE_URL = "jdbc:mysql://localhost/restaurant?useSSL=false";
	private static final String DATABASE_USERNAME = "root";
	private static final String DATABASE_PASSWORD = "mysql";
	private static final String SELECT_QUERY = "SELECT * FROM users WHERE username = ? AND password = ?";
	
	public boolean validate(String username, String password) {
		//baglantiyi kur
		try {
			Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			
			System.out.println(preparedStatement);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
				
			}
			//hata k�sm�
		} catch (SQLException e) {
			printSQLException(e);
		}
		return false;
		
	}
	
	public Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection(DATABASE_URL,DATABASE_USERNAME,DATABASE_PASSWORD);
            return connection;
        }catch(SQLException ex){
            printSQLException(ex);
        }
        return null;
    }
	
	public static void printSQLException(SQLException ex) {
		for(Throwable e: ex) {
			if(ex instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException)e).getSQLState());
				System.err.println("Error Code: " + ((SQLException)e).getErrorCode());
				System.err.println("Message: " + ex.getMessage());
				System.err.println("Message: " + ex.getMessage());
				Throwable t = ex.getCause();
				while(t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
}
