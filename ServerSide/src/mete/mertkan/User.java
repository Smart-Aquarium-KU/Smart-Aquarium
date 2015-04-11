package mete.mertkan;

public class User {

	private int id;
	private String username;
	private String userpass;
	private String salt;
	private String email;
	private int gender; //0 means female 1 means male
	private String hash_name;
	private int roleId;
	
	public User(String username,String userpass){
		this.username=username;
		this.userpass=userpass;
	}
	public User(){
		
	}
	public User(User user){
		this.id=user.getId();
		this.username=user.getUsername();
		this.userpass=user.getUserpass();
		this.salt=user.getSalt();
		this.email=user.getEmail();
		this.gender=user.getGender();
		this.hash_name=user.getHash_name();
		this.roleId=user.getRoleId();
	}
	public User(String username){
		this.username=username;
	}
	//Getters and setters
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getHash_name() {
		return hash_name;
	}
	public void setHash_name(String hash_name) {
		this.hash_name = hash_name;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
}
