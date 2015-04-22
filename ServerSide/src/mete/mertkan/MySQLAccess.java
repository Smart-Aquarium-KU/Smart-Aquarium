package mete.mertkan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



public class MySQLAccess {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost/test";

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
	//When a new user added make that user guess
	public void makeGuessLastId(Connection con){
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			//read the last entered user id
			ps = con.prepareStatement("select max(user_id) as last_id from users");
			rs = ps.executeQuery();
			int lastId=0;
			if (rs.next()) {
				lastId = rs.getInt("last_id");

			}

			if(lastId==0)
				System.out.println("Last id readed as 0");
			else{
				//make that user guess
				ps = con.prepareStatement("INSERT INTO role_user(role_id,user_id) VALUES (?,?)");
				ps.setInt(1,GUESSUSER);
				ps.setInt(2, lastId);
				ps.executeUpdate();

			}



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(rs);
			close(ps);
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

			ps = con.prepareStatement("INSERT INTO users(username,password,email,gender,salt) VALUES (?,?,?,?,?)");
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
	//read the hash_word of the user
	public String readHash_word(Connection con,String username){
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement("SELECT hash_word FROM users WHERE username = ?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			String hash_word="";
			if (rs.next()) {
				hash_word = rs.getString("hash_word");
				// DATABASE VALIDATION
				if (hash_word == "") {
					return null;
				}
				if (rs.next()) { // Should not append, because login is the primary key
					throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
				}
			} else { // TIME RESISTANT ATTACK (Even if the user does not exist the
				// Computation time is equal to the time needed for a legitimate user
				return null;
			}

			return hash_word;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public int[] readIdWithRole(Connection con,String username){
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement("SELECT u.user_id,ru.role_id FROM users u join role_user ru on u.user_id=ru.user_id WHERE username = ?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			int id=0, roleid=0;
			if (rs.next()) {
				id = rs.getInt("user_id");
				roleid = rs.getInt("role_id");
				// DATABASE VALIDATION
				if (id == 0 || roleid == 0) {
					throw new SQLException("Database inconsistant id or roleid altered");
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

			ps = con.prepareStatement("SELECT password, salt FROM users WHERE username = ?");
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
	public User readUsersInfo(Connection con, String username) throws SQLException,IOException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user=new User(username);
		ps = con.prepareStatement("SELECT password,email,gender,salt FROM users WHERE username = ?");
		ps.setString(1, username);
		rs = ps.executeQuery();
		int gender;
		String password, email,salt;
		if (rs.next()) {
			password = rs.getString("password");
			email = rs.getString("email");
			gender=rs.getInt("gender");
			salt = rs.getString("salt");

			// DATABASE VALIDATION
			if (password == null || salt == null) {
				throw new SQLException("Database inconsistant Salt or Digested Password altered");
			}
			if (rs.next()) { // Should not append, because login is the primary key
				throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
			}
			//if everything is okay
			user.setUserpass(password);
			user.setEmail(email);
			user.setGender(gender);
			user.setSalt(salt);

		} else { // TIME RESISTANT ATTACK (Even if the user does not exist the
			// Computation time is equal to the time needed for a legitimate user

		}

		close(rs);
		close(ps);

		return user;


	}
	public int[] getVisiableAquariumsForGuess(Connection con, int id) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = con.prepareStatement("SELECT aquarium_id FROM access WHERE user_id = ?");
		ps.setInt(1, id);
		rs = ps.executeQuery();

		List<Integer> aquariums = new ArrayList<Integer>();
		while (rs.next()) {
			aquariums.add(rs.getInt("aquarium_id"));
			// DATABASE VALIDATION
			if (aquariums.isEmpty()) {
				throw new SQLException("Database inconsistant Salt or Digested Password altered");
			}

		} 

		close(rs);
		close(ps);

		return toIntArray(aquariums);
	}
	int[] toIntArray(List<Integer> list)  {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)  
			ret[i++] = e.intValue();
		return ret;
	}
	public Aquarium getAquariumFromId(Connection con,int id) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = con.prepareStatement("SELECT aquarium_name,mac_address,ip_address,owner_id FROM aquariums WHERE aquarium_id = ?");
		ps.setInt(1, id);
		rs = ps.executeQuery();
		String aquarium_name="",mac_address="",ip_address="";
		int owner_id=0;
		if (rs.next()) {
			aquarium_name = rs.getString("aquarium_name");
			mac_address=rs.getString("mac_address");
			ip_address=rs.getString("ip_address");
			owner_id=rs.getInt("owner_id");
			// DATABASE VALIDATION
			if (aquarium_name == null || aquarium_name == "" || owner_id==0) {
				throw new SQLException("Database inconsistant Salt or Digested Password altered");
			}
			if (rs.next()) { // Should not append, because login is the primary key
				throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
			}
		} else { // TIME RESISTANT ATTACK (Even if the user does not exist the
			// Computation time is equal to the time needed for a legitimate user

		}

		close(rs);
		close(ps);

		return new Aquarium(id,aquarium_name,mac_address,ip_address,owner_id);
	}

	public void createAquarium(Connection con, Aquarium aquarium) throws SQLException{
		PreparedStatement ps = null;


		ps = con.prepareStatement("INSERT INTO aquariums(aquarium_name,mac_address,ip_address,owner_id) VALUES (?,?,?,?)");
		ps.setString(1,aquarium.getAquarium_name());
		ps.setString(2,aquarium.getMac_address());
		ps.setString(3,aquarium.getIp_address());
		ps.setInt(4,aquarium.getOwner_id());
		ps.executeUpdate();

		//insert the last id to role table as guess user
		convertGuestToNormalUser(con,aquarium.getOwner_id());
		close(ps);

	}
	
	public void convertGuestToNormalUser(Connection con,int id) throws SQLException{
		PreparedStatement ps = null;


		ps = con.prepareStatement("UPDATE role_user SET role_id=? WHERE user_id=?");
		ps.setInt(1,1);
		ps.setInt(2,id);
		ps.executeUpdate();

		//update the user guest to normal
		
		close(ps);
	}
	
	public void insertHashWord(Connection con,String hash_word,int id) throws SQLException{
		PreparedStatement ps = null;


		ps = con.prepareStatement("UPDATE users SET hash_word=? WHERE user_id=?");
		ps.setString(1,hash_word);
		ps.setInt(2,id);
		ps.executeUpdate();

		//update the user guest to normal
		
		close(ps);
	}
	public int[] getAquariumsOfUser(Connection con, int id) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = con.prepareStatement("SELECT aquarium_id FROM aquariums WHERE owner_id = ?");
		ps.setInt(1, id);
		rs = ps.executeQuery();

		List<Integer> aquariums = new ArrayList<Integer>();
		while (rs.next()) {
			aquariums.add(rs.getInt("aquarium_id"));
			// DATABASE VALIDATION
			if (aquariums.isEmpty()) {
				throw new SQLException("Database inconsistant Salt or Digested Password altered");
			}

		} 
		close(rs);
		close(ps);

		return toIntArray(aquariums);
	}
	public int getAquariumsLastTemp(Connection con,int id) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = con.prepareStatement("SELECT degree from log_temperature where log_id=(select max(log_id) from log_temperature where aquarium_id=?)");
		ps.setInt(1, id);
		rs = ps.executeQuery();

		int degree=0;
		if (rs.next()) {
			degree=rs.getInt("degree");
			// DATABASE VALIDATION
			if (degree==0) {
				throw new SQLException("Database inconsistant Salt or Digested Password altered");
			}

		} 
		close(rs);
		close(ps);

		return degree;
	}
	public void givePermission(Connection con,int userId,int aquariumId) throws SQLException{
		PreparedStatement ps = null;


		ps = con.prepareStatement("INSERT INTO access(user_id,aquarium_id) VALUES(?,?)");
		ps.setInt(1,userId);
		ps.setInt(2,aquariumId);
		ps.executeUpdate();

		close(ps);
	}

}
