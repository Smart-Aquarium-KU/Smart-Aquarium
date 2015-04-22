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
 * Servlet implementation class userPanel
 */
@WebServlet("/userPanel")
public class userPanel extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public userPanel() {
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
		User user=new User((User)request.getAttribute("user")); 
		MySQLAccess accessControl= new MySQLAccess();
		Connection con=null;
		con=accessControl.connect(con);

		//Aquariums that user have
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
		
		
		//add first aquarium to session for future it will be change
		request.getSession().setAttribute("aquarium", aquariumArray[0]);
		
		
		aquariums.clear();
		aquariumIds=null;

		//User's aquariums that can watch
		try {
			aquariumIds=accessControl.getVisiableAquariumsForGuess(con, user.getId());
			for (int i = 0; i < aquariumIds.length; i++) {
				aquariums.add(accessControl.getAquariumFromId(con, aquariumIds[i]));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Aquarium[] guestAquariumArray=aquariums.toArray(new Aquarium[aquariums.size()]);

		
		PrintWriter out =response.getWriter();
		StringBuilder guessPage=new StringBuilder();
		guessPage.append(
				"<!DOCTYPE html>"+"<html>"+ 
						"<head>"+
						"<script>"+
						"function myFunction(ipAddress) {"+
						"document.getElementById(\"video\").src =\"http://\"+ipAddress+\"\";}" +
						"</script>"+
						"<title>Smart Aquarium</title>"+
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
						"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> "+
						"<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
						"<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
						"<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
						"</head>"+
						"<body>" +
						"<div data-role=\"page\"  data-title=\"Smart Aquarium\">"+ 
						"<div data-role=\"header\" data-theme=\"a\"><h1> </h1></div>"+ 
						"<div data-role=\"content\" data-theme=\"a\">"+
						"<center><img src=\"pi.png\"></center> <br> </div>" +
						"<a href=\"#feed\" data-rel=\"popup\" data-role=\"button\" data-transition=\"flow\" data-theme=\"a\"  >Feed Your Fish!</a>" +
						"<div data-role=\"popup\" id=\"feed\" data-overlay-theme=\"a\" data-theme=\"c\" data-dismissible=\"false\" style=\"max-width:400px;\" class=\"ui-corner-all\">" +
						"<div data-role=\"header\" data-theme=\"a\" class=\"ui-corner-top\">" +
						"<h1>Feed ?</h1>" +
						"</div>" +
						" <div data-role=\"content\" data-theme=\"d\" class=\"ui-corner-bottom ui-content\">" +
						"<h3 class=\"ui-title\">Are you sure you want to feed ?</h3>" +
						"<p>This action cannot be undone.</p>" +
						"<a href=\"#\" data-role=\"button\" data-inline=\"true\" data-rel=\"back\" data-theme=\"c\">Cancel</a>" +
						" <a href=\"#\" data-role=\"button\" data-inline=\"true\" data-rel=\"back\" data-transition=\"flow\" data-theme=\"b\">Feed</a>" +
						"</div> </div>"+
						"<div data-role=\"header\">"+
						"<h2>Smart Aquarium</h2>"+
						"</div>"+
						"<center><h4>Welcome "+user.getUsername()+"</h4></center>"+
						"<form name=\"addAquarium\" action=\"askFish.html\" method=\"post\">"+
						"<input type=\"submit\" value=\"ADD AQUARIUM\"/>  "+
				"</form>"+
				"<form name=\"lights\" action=\"lights\" method=\"post\">"+
				"<input type=\"submit\" value=\"LIGHTS ON/OFF\"/>  "+
		"</form>");
		for (int i = 0; i < aquariumArray.length; i++) {
			//guessPage.append("<div class=\"ui-block-a\"><button  onclick=\"myFunction('"+aquariumArray[i].getIp_address()+"')\" type=\"submit\" data-theme=\"c\">"+aquariumArray[i].getAquarium_name()+"</button></div>");
			guessPage.append("<input type=\"submit\" onclick=\"myFunction('"+aquariumArray[i].getIp_address()+"');\" value=\""+aquariumArray[i].getAquarium_name()+"\"/>  ");
		}
		guessPage.append("<center>Other's Fish</center>");
		for (int i = 0; i < guestAquariumArray.length; i++) {
			guessPage.append("<input type=\"submit\" onclick=\"myFunction('"+guestAquariumArray[i].getIp_address()+"');\" value=\""+guestAquariumArray[i].getAquarium_name()+"\"/>  ");
		}
		guessPage.append("<iframe id=\"video\" src=\"fish.html\" width=\"640\" height=\"480\"></iframe>");
		guessPage.append("<div data-role=\"footer\" data-position=\"fixed\" data-theme=\"d\"><h1>Kasetsart University Senior Project </h1></div> "+
				"<div name=\"userId\" style=\"display: none;\">"+user.getId()+"</div> " +
						"<form name=\"temperature\" action=\"temperature\" method=\"post\">" +
						"<input type=\"submit\" value=\"TEMPERATURES\">" +
						"</form>" +
						"<form name=\"addGuest\" action=\"addGuest.html\" method=\"post\">" +
						"<input type=\"submit\" value=\"ADD GUEST\">" +
						"</form>" +
				"</body>"+
				"</html>");
		out.println(guessPage.toString());
		out.close();
		
	}

}
