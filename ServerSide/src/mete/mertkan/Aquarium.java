package mete.mertkan;

public class Aquarium {
	private int aquarium_id;
	private String aquarium_name;
	private String  mac_address;
	private String  ip_address;
	private int owner_id;
	
	public Aquarium(int aquarium_id,String aquarium_name,String  mac_address,String  ip_address,int owner_id){
		this.aquarium_id=aquarium_id;
		this.aquarium_name=aquarium_name;
		this.mac_address=mac_address;
		this.ip_address=ip_address;
		this.owner_id=owner_id;
	}
	public Aquarium(int aquarium_id){
		this.aquarium_id=aquarium_id;
	}
	public Aquarium(String aquarium_name,String  mac_address,String  ip_address,int owner_id){
		this.aquarium_name=aquarium_name;
		this.mac_address=mac_address;
		this.ip_address=ip_address;
		this.owner_id=owner_id;
	}
	
	public int getAquarium_id() {
		return aquarium_id;
	}
	public void setAquarium_id(int aquarium_id) {
		this.aquarium_id = aquarium_id;
	}
	public String getAquarium_name() {
		return aquarium_name;
	}
	public void setAquarium_name(String aquarium_name) {
		this.aquarium_name = aquarium_name;
	}
	public String getMac_address() {
		return mac_address;
	}
	public void setMac_address(String mac_address) {
		this.mac_address = mac_address;
	}
	public String getIp_address() {
		return ip_address;
	}
	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}
	public int getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}
	
	
	

}
