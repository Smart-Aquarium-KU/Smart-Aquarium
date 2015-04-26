package mete.mertkan.objects;

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

	private static Connection con=null;

	private static MySQLAccess access=null;

	private MySQLAccess(){}

	public static MySQLAccess getConnection(){
		initObject();
		return access;
	}

	private static void initObject(){
		synchronized (MySQLAccess.class) {
			if (access == null) {
				access = new MySQLAccess();
				con=connect(con);
			}
		}

	}

	//Connectiong to database
	private static Connection connect(Connection con){
		try {
			Class.forName(JDBC_DRIVER);
			con = DriverManager.getConnection(DB_URL,USER,PASS);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return con;
	}

	//From a byte[] returns a base 64 representation
	public  String byteToBase64(byte[] data){
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}

	// From a base 64 representation, returns the corresponding byte[] 
	public  byte[] base64ToByte(String data){
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			return decoder.decodeBuffer(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	//Closes the current resultset
	public void close(ResultSet rs){
		if (rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//Closes the current statement
	public void close(Statement ps){
		if (ps!=null){	
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// From a password, a number of iterations and a salt,returns the corresponding digest
	public byte[] getHash(int iterationNb, String password, byte[] salt){
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			digest.reset();
			digest.update(salt);
			byte[] input = digest.digest(password.getBytes("UTF-8"));
			for (int i = 0; i < iterationNb; i++) {
				digest.reset();
				input = digest.digest(input);
			}
			return input;

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	//When a new user added make that user guess
	public void makeLastEnteredIdGuest(){
		PreparedStatement ps = null;
		ResultSet rs = null;

		//read the last entered user id
		try {
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

			close(rs);
			close(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// Inserts a new user in the database
	public boolean createUser(User user){

		try {
			PreparedStatement ps = null;

			// Uses a secure Random not a simple Random
			SecureRandom random;

			random = SecureRandom.getInstance("SHA1PRNG");
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
			makeLastEnteredIdGuest();

			close(ps);
			return true;
		} catch (NoSuchAlgorithmException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}




	}

	// Authenticates the user with a given login and password
	// If password and/or login is null then always returns false.
	// If the user does not exist in the database returns false.
	public boolean authenticate(String username, String password){
		

		try {
			PreparedStatement ps = null;
			ResultSet rs = null;

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

			close(rs);
			close(ps);
			return Arrays.equals(proposedDigest, bDigest) && userExist;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	public User readUsersInfo(String username){
		
		try {
			PreparedStatement ps = null;
			ResultSet rs = null;
			User user=new User(username);
			ps = con.prepareStatement("select u.user_id,password,email,gender,salt,hash_word,role_id from users u join role_user ru on u.user_id=ru.user_id where username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			int user_id,role_id,gender;
			String password, email,salt,hash_word;
			if (rs.next()) {
				user_id=rs.getInt("user_id");
				password = rs.getString("password");
				email = rs.getString("email");
				gender=rs.getInt("gender");
				salt = rs.getString("salt");
				hash_word=rs.getString("hash_word");
				role_id=rs.getInt("role_id");

				// DATABASE VALIDATION
				if (password == null || salt == null) {
					throw new SQLException("Database inconsistant Salt or Digested Password altered");
				}
				if (rs.next()) { // Should not append, because login is the primary key
					throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
				}
				//if everything is okay
				user.setId(user_id);
				user.setUserpass(password);
				user.setEmail(email);
				user.setGender(gender);
				user.setSalt(salt);
				user.setHash_name(hash_word);
				user.setRoleId(role_id);


			} else { // TIME RESISTANT ATTACK (Even if the user does not exist the
				// Computation time is equal to the time needed for a legitimate user

			}

			close(rs);
			close(ps);

			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		


	}
	public int[] getVisiableAquariumsForGuest(int id){
		
		try {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	int[] toIntArray(List<Integer> list)  {
		int[] ret = new int[list.size()];
		int i = 0;
		for (Integer e : list)  
			ret[i++] = e.intValue();
		return ret;
	}
	public Aquarium getAquariumFromId(int id){
		
		try {
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
			Aquarium aq=new Aquarium(aquarium_name, mac_address, ip_address, owner_id);
			aq.setAquarium_id(id);

			return aq;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public void createAquarium(Aquarium aquarium){
		try {
			PreparedStatement ps = null;
			ps = con.prepareStatement("INSERT INTO aquariums(aquarium_name,mac_address,ip_address,owner_id) VALUES (?,?,?,?)");
			ps.setString(1,aquarium.getAquarium_name());
			ps.setString(2,aquarium.getMac_address());
			ps.setString(3,aquarium.getIp_address());
			ps.setInt(4,aquarium.getOwner_id());
			ps.executeUpdate();

			//insert the last id to role table as guess user
			convertGuestToNormalUser(aquarium.getOwner_id());
			close(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public void convertGuestToNormalUser(int id){
		//update the user guest to normal	
		try {
			PreparedStatement ps = null;
			ps = con.prepareStatement("UPDATE role_user SET role_id=? WHERE user_id=?");
			ps.setInt(1,1);
			ps.setInt(2,id);
			ps.executeUpdate();
			close(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void insertHashWord(String hash_word,int id){
		
		try {
			PreparedStatement ps = null;
			ps = con.prepareStatement("UPDATE users SET hash_word=? WHERE user_id=?");
			ps.setString(1,hash_word);
			ps.setInt(2,id);
			ps.executeUpdate();
			close(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public int[] getAquariumsOfUser(int id){
		
		try {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public int getAquariumsLastTemp(int id){
		
		try {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	public void givePermission(int userId,int aquariumId){
		try {
			PreparedStatement ps = null;
			ps = con.prepareStatement("INSERT INTO access(user_id,aquarium_id) VALUES(?,?)");
			ps.setInt(1,userId);
			ps.setInt(2,aquariumId);
			ps.executeUpdate();
			close(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
