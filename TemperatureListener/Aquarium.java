
public class Aquarium {
	private int id;
	private String name;
	private int temperature;
	
	public Aquarium(int id,String name,int temperature){
		this.id=id;
		this.name=name;
		this.temperature=temperature;
	}
	public void printAquarium(){
		System.out.println("id: "+id+" name:"+name+" temperature:"+temperature);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	

}
