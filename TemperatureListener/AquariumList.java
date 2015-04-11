import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AquariumList {

	private List<Aquarium> list;

	public AquariumList(){
		list= new ArrayList<Aquarium>();
	}
	public void add(Aquarium aquarium){

		int id=aquarium.getId();
		int index=0;
		boolean found=false;
		if(list.size()!=0){

			for(Aquarium element:list){
				if(element.getId()==id){
					list.set(index,aquarium);	
					found=true;
				}
				index++;
			}

			if(!found){
				list.add(aquarium);
			}
		}
		else
			list.add(aquarium);
	}
	public int size(){
		return list.size();

	}
	public void printList(){
		Iterator<Aquarium> iter = list.iterator();
		while(iter.hasNext()){
			iter.next().printAquarium();	
		}
	}
	public void writeToDatabase(){
		try {
			MySQLAccessHandler handler= new MySQLAccessHandler();
			Connection con=null;
			con=handler.connect(con);

			Iterator<Aquarium> iter = list.iterator();
			while(iter.hasNext()){
				handler.writeTemperature(con,iter.next());	
			}

			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
