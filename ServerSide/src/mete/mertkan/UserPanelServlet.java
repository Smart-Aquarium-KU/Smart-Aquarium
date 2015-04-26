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

@WebServlet("/userPanel")
public class UserPanelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public UserPanelServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get the user object from session
		User user=new User((User)request.getSession().getAttribute("user"));

		//get the database object
		MySQLAccess accessControl=MySQLAccess.getConnection();

		//Aquariums that user have
		int[] aquariumIds;
		List<Aquarium> aquariums= new ArrayList<Aquarium>();

		aquariumIds=accessControl.getAquariumsOfUser(user.getId());
		for (int i = 0; i < aquariumIds.length; i++) {
			aquariums.add(accessControl.getAquariumFromId(aquariumIds[i]));

		}

		Aquarium[] aquariumArray=aquariums.toArray(new Aquarium[aquariums.size()]);


		//add first aquarium to session for future it will be change
		request.getSession().setAttribute("aquarium", aquariumArray[0]);


		aquariums.clear();
		aquariumIds=null;

		//User's aquariums that can watch

		aquariumIds=accessControl.getVisiableAquariumsForGuest(user.getId());
		for (int i = 0; i < aquariumIds.length; i++) {
			aquariums.add(accessControl.getAquariumFromId(aquariumIds[i]));
		}

		Aquarium[] guestAquariumArray=aquariums.toArray(new Aquarium[aquariums.size()]);

		//create the string for html
		PrintWriter out =response.getWriter();
		StringBuilder guessPage=new StringBuilder();
		guessPage.append(
				"<!DOCTYPE html>"+"<html>"+ 
						"<head>"+
						"<script>"+
						"function myFunction(ipAddress) {"+
						"document.getElementById(\"video\").src =\"http://\"+ipAddress+\":8081\"+\"\";}" +
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
						"<div data-role=\"header\" data-theme=\"a\"><h1>Smart Aquarium </h1></div>"+ 
						"<div data-role=\"content\" data-theme=\"a\">"+
						"<center><img src=\"pi.png\"></center> <br> </div>" +
						"<center><h4>Welcome "+user.getUsername()+"</h4></center>"+
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
		guessPage.append("<iframe id=\"video\" src=\"fish.html\" width=\"352\" height=\"288\"></iframe>");
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
