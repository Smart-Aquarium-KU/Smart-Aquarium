import java.sql.*;
import com.mysql.jdbc.Driver;

public class MySQLAccess {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/SmartAquarium";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "mete123";

	public boolean valideUser(String username,String password)
	{
		Connection conn = null;
		Statement stmt = null;
		int id=0;
		try {

			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT id, username, password FROM user WHERE username='"+username+"'"+" and "+"password='"+password+"'";
			//System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				//Display values
				System.out.println("ID: " + id+", Username: " + username);
			}
			rs.close();
			stmt.close();
			conn.close();

			if(id!=0)
			{
				System.out.println("Successfully readed");
				return true;
			}
			else{
				System.out.println("User Could not found with username:"+username);
				return false;
			}
			//STEP 6: Clean-up environment

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
}
