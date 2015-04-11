import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLAccessHandler {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/test";

	//  Database credentials
	private static final String USER = "root";
	private static final String PASS = "mete123";
	
	
	//Connectiong to database
		public Connection connect(Connection con){
			
			try {
				Class.forName(JDBC_DRIVER);
				con = DriverManager.getConnection(DB_URL,USER,PASS);
				return con;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}catch(SQLException e){
				e.printStackTrace();
				return null;
			}
		}
		
		//Closes the current resultset
		public void close(ResultSet rs) {
			if (rs!=null){
				try {
					rs.close();
				} catch (SQLException ignore) {
				}
			}
		}
		//Closes the current statement
		public void close(Statement ps) {
			if (ps!=null){
				try {
					ps.close();
				} catch (SQLException ignore) {
				}
			}
		}
		
		public boolean writeTemperature(Connection con,Aquarium aquarium){
			PreparedStatement ps = null;
			
			try {
				ps = con.prepareStatement("INSERT INTO log_temperature(aquarium_id,degree) VALUES (?,?)");
				ps.setInt(1, aquarium.getId());
				ps.setDouble(2,aquarium.getTemperature());
				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
		}
}
