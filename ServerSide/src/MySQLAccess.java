import java.sql.*;
import com.mysql.jdbc.Driver;

public class MySQLAccess {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/SmartAquarium";

	//  Database credentials
	private static final String USER = "root";
	private static final String PASS = "mete123";
	
	//user types
	private static final int FAIL=0;
	private static final int NORMALUSER=1;
	private static final int ADMINUSER=2;
	private static final int GUESSUSER=3;


	public int valideUser(User user)
	{
		Connection conn = null;
		PreparedStatement pstmt;
		try {
			int roleid=0;
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			String sql = "SELECT roleid,id FROM user u join user_role ur on u.id=ur.userid" +
					" WHERE username= ? and password= ?";
			//Prepared Statement for defending the sql injection
			pstmt= conn.prepareStatement(sql);
			pstmt.setString(1,user.getUsername());
			pstmt.setString(2,user.getUserpass());
			
			ResultSet rs = pstmt.executeQuery();
			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				roleid  = rs.getInt("roleid");
				int id =rs.getInt("id");
				//Display values
				System.out.println("ID: " + id+", Username: " + user.getUsername());
			}
			//STEP 6: Clean-up environment
			rs.close();
			pstmt.close();
			conn.close();

			if(roleid!=0)
			{
				System.out.println("Successfully readed");
				if(roleid==1)
					return NORMALUSER;
				else if(roleid==2)
					return ADMINUSER;
				else
					return GUESSUSER;
			}
			else{
				System.out.println("User Could not found with username:"+user.getUsername());
				return FAIL;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return FAIL;
		}

	}
}
