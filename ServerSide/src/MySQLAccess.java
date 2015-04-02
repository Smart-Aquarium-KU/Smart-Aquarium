import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Arrays;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class MySQLAccess {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/SmartAquarium";

	//  Database credentials
	private static final String USER = "root";
	private static final String PASS = "mete123";

	//user types
	private static final int NORMALUSER=1;
	private static final int ADMINUSER=2;
	private static final int GUESSUSER=3;

	private final static int ITERATION_NUMBER = 1000;

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

	//From a byte[] returns a base 64 representation
	public  String byteToBase64(byte[] data){
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}

	// From a base 64 representation, returns the corresponding byte[] 
	public  byte[] base64ToByte(String data) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(data);
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
	// From a password, a number of iterations and a salt,returns the corresponding digest
	public byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(salt);
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}

	public void makeGuessLastId(Connection con){
		PreparedStatement ps = null;
		ResultSet rs = null;
		
			try {
				//read the last entered user id
				ps = con.prepareStatement("select max(id) as last_id from user");
				rs = ps.executeQuery();
				int lastId=0;
					if (rs.next()) {
						lastId = rs.getInt("last_id");

					}
					
					if(lastId==0)
						System.out.println("Last id readed as 0");
					else{
						//make that user guess
						ps = con.prepareStatement("INSERT INTO user_role(userid,roleid) VALUES (?,?)");
						ps.setInt(1, lastId);
						ps.setInt(2, GUESSUSER);
						ps.executeUpdate();
						
					}
					
					
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	}
	// Inserts a new user in the database
	public boolean createUser(Connection con, User user){
		PreparedStatement ps = null;
		try {
				// Uses a secure Random not a simple Random
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				// Salt generation 64 bits long
				byte[] bSalt = new byte[8];
				random.nextBytes(bSalt);
				// Digest computation
				byte[] bDigest = getHash(ITERATION_NUMBER,user.getUserpass(),bSalt);
				String sDigest = byteToBase64(bDigest);
				String sSalt = byteToBase64(bSalt);

				ps = con.prepareStatement("INSERT INTO user(username, password,email,gender,salt) VALUES (?,?,?,?,?)");
				ps.setString(1,user.getUsername());
				ps.setString(2,sDigest);
				ps.setString(3,user.getEmail());
				ps.setInt(4,user.getGender());
				ps.setString(5,sSalt);
				ps.executeUpdate();
				
				//insert the last id to role table as guess user
				makeGuessLastId(con);
				
				return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			return false;
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return false;
		} finally {
			close(ps);
		}
}

public int[] readIdWithRole(Connection con,String username){
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = con.prepareStatement("SELECT id,roleid FROM user u join user_role ur on u.id=ur.userid WHERE username = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		int id=0, roleid=0;
		if (rs.next()) {
			id = rs.getInt("id");
			roleid = rs.getInt("roleid");
			// DATABASE VALIDATION
			if (id == 0 || roleid == 0) {
				throw new SQLException("Database inconsistant Salt or Digested Password altered");
			}
			if (rs.next()) { // Should not append, because login is the primary key
				throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
			}
		} else { // TIME RESISTANT ATTACK (Even if the user does not exist the
			// Computation time is equal to the time needed for a legitimate user
			return null;
		}
		return new int[]{id,roleid};
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}
	finally{
		close(rs);
		close(ps);
	}
}
	// Authenticates the user with a given login and password
	// If password and/or login is null then always returns false.
	// If the user does not exist in the database returns false.
	public boolean authenticate(Connection con, String username, String password)
			throws SQLException, NoSuchAlgorithmException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			boolean userExist = true;

			ps = con.prepareStatement("SELECT password, salt FROM user WHERE username = ?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			String digest, salt;
			if (rs.next()) {
				digest = rs.getString("password");
				salt = rs.getString("salt");
				// DATABASE VALIDATION
				if (digest == null || salt == null) {
					throw new SQLException("Database inconsistant Salt or Digested Password altered");
				}
				if (rs.next()) { // Should not append, because login is the primary key
					throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
				}
			} else { // TIME RESISTANT ATTACK (Even if the user does not exist the
				// Computation time is equal to the time needed for a legitimate user
				digest = "000000000000000000000000000=";
				salt = "00000000000=";
				userExist = false;
			}

			byte[] bDigest = base64ToByte(digest);
			byte[] bSalt = base64ToByte(salt);

			// Compute the new DIGEST
			byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

			return Arrays.equals(proposedDigest, bDigest) && userExist;
		} catch (IOException ex){
			throw new SQLException("Database inconsistant Salt or Digested Password altered");
		}
		finally{
			close(rs);
			close(ps);
		}
	}

}
