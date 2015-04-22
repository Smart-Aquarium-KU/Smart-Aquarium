package mete.mertkan;



import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class guessPanel
 */
@WebServlet("/guestPanel")
public class guestPanel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public guestPanel() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		User user=new User((User)request.getAttribute("user")); 
		PrintWriter out =response.getWriter();
		MySQLAccess accessControl= new MySQLAccess();
		Connection con=null;
		con=accessControl.connect(con);
		int[] aquariumIds;
		List<Aquarium> aquariums= new ArrayList<Aquarium>();
		try {
			aquariumIds=accessControl.getVisiableAquariumsForGuess(con, user.getId());
			for (int i = 0; i < aquariumIds.length; i++) {
				aquariums.add(accessControl.getAquariumFromId(con, aquariumIds[i]));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Aquarium[] aquariumArray=aquariums.toArray(new Aquarium[aquariums.size()]);
		request.getSession().setAttribute("user",user);
		StringBuilder guessPage=new StringBuilder();
		guessPage.append(
				"<!DOCTYPE html>"+"<html>"+ 
						"<head>"+
						"<script>"+
						"function myFunction(ipAddress) {"+
						"document.getElementById(\"video\").src =\"http://\"+ipAddress+\"\";}" +
						"</script>"+
						"<title>Smart Aquarium</title>"+
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
						"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> "+
						"<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
						"<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
						"<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
						"</head>"+
						"<body>"+
						"<div data-role=\"header\">"+
						"<h2>Smart Aquarium</h2>"+
						"</div>"+
						"<center><h4>Welcome "+user.getUsername()+"</h4></center>"+
						"<form name=\"addAquarium\" action=\"askFish.html\" method=\"post\">"+
						"<input type=\"submit\" value=\"ADD AQUARIUM\"/>  "+
				"</form>");
		//add the aquariums that user can see
		for (int i = 0; i < aquariumArray.length; i++) {
			//guessPage.append("<div class=\"ui-block-a\"><button  onclick=\"myFunction('"+aquariumArray[i].getIp_address()+"')\" type=\"submit\" data-theme=\"c\">"+aquariumArray[i].getAquarium_name()+"</button></div>");
			guessPage.append("<input type=\"submit\" onclick=\"myFunction('"+aquariumArray[i].getIp_address()+"');\" value=\""+aquariumArray[i].getAquarium_name()+"\"/>  ");
		}
		guessPage.append("<iframe id=\"video\" src=\"fish.html\" width=\"640\" height=\"480\"></iframe>");
	

	guessPage.append("<div data-role=\"footer\" data-position=\"fixed\" data-theme=\"d\"><h1>Kasetsart University Senior Project </h1></div> "+
			"<div name=\"userId\" style=\"display: none;\">"+user.getId()+"</div> " +
			"</body>"+
			"</html>");
	out.println(guessPage.toString());
	out.close();
	}

}
