package mete.mertkan;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mete.mertkan.objects.Aquarium;
import mete.mertkan.objects.MySQLAccess;
import mete.mertkan.objects.User;

@WebServlet("/temperature")
public class TemperaturePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public TemperaturePageServlet() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get the user object from session
		User user=new User((User)request.getSession().getAttribute("user"));

		//get the database object
		MySQLAccess accessControl= MySQLAccess.getConnection();

		//get the aquariums that user can see
		int[] aquariumIds;
		List<Aquarium> aquariums= new ArrayList<Aquarium>();
		aquariumIds=accessControl.getAquariumsOfUser(user.getId());
		for (int i = 0; i < aquariumIds.length; i++) {
			aquariums.add(accessControl.getAquariumFromId(aquariumIds[i]));
		}

		//create the aquariums
		Aquarium[] aquariumArray=aquariums.toArray(new Aquarium[aquariums.size()]);

		//get its temperatures 
		int[] temp=new int[aquariumArray.length];
		for (int i = 0; i < aquariumArray.length; i++) {

			temp[i]=accessControl.getAquariumsLastTemp(aquariumArray[i].getAquarium_id());

		}


		//Create the html file for temperatures
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
