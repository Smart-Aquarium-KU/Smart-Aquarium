

public class WriteDatabasePeriodically implements Runnable {

	AquariumList list;
	public WriteDatabasePeriodically(AquariumList aqList){
		list=aqList;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		print();
		writeToDatabase();
	}
	public void print(){	
			list.printList();
	}
	public void writeToDatabase(){
		list.writeToDatabase();
		System.out.println("Successfully writed to database!!");
	}
	

}
