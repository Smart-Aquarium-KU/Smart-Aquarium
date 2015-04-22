package mete.mertkan;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class temperature
 */
@WebServlet("/temperature")
public class temperature extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public temperature() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User user=new User((User)request.getSession().getAttribute("user"));
		MySQLAccess accessControl= new MySQLAccess();
		Connection con=null;
		con=accessControl.connect(con);
		
		int[] aquariumIds;
		List<Aquarium> aquariums= new ArrayList<Aquarium>();
		try {
			aquariumIds=accessControl.getAquariumsOfUser(con, user.getId());
			for (int i = 0; i < aquariumIds.length; i++) {
				aquariums.add(accessControl.getAquariumFromId(con, aquariumIds[i]));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Aquarium[] aquariumArray=aquariums.toArray(new Aquarium[aquariums.size()]);
		
		int[] temp=new int[aquariumArray.length];
		for (int i = 0; i < aquariumArray.length; i++) {
			try {
				temp[i]=accessControl.getAquariumsLastTemp(con, aquariumArray[i].getAquarium_id());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PrintWriter out =response.getWriter();
		StringBuilder temperature=new StringBuilder();
		temperature.append(
				"<!DOCTYPE html>"+"<html>"+ 
						"<head></head>" +
						"<body>");
		for (int i = 0; i < temp.length; i++) {
			temperature.append(aquariumArray[i].getAquarium_name()+"---->"+temp[i]+"C"+"</br>");
		}
		
		temperature.append("</body>"+
				"</html>");
		
		out.println(temperature.toString());
		out.close();
		
	}

}
