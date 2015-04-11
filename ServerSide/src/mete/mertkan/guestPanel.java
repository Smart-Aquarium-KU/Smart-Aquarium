package mete.mertkan;



import java.io.IOException;
import java.io.PrintWriter;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		User user=new User((User)request.getAttribute("user")); 
		PrintWriter out =response.getWriter();
		out.println("<!DOCTYPE html>"+
				"<html>"+ 
				"<head>"+
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
				"<form name=\"loginForm\" action=\"askFish\" method=\"post\">"+
				"<input type=\"submit\" value=\"ADD AQUARIUM\"/>  "+
				"</form>"+
				"<object data=\"http://www.ibrahimdilaver.blogspot.com\" width=\"100%\" height=\"300\"> <embed src=\"http://ibrahimdilaver.blogspot.com\" width=\"100%\" height=\"400\"> </embed> Error: Embedded data could not be displayed. </object>"+
				"<fieldset class=\"ui-grid-b\">"+
				"<div class=\"ui-block-a\"><button type=\"submit\" data-theme=\"c\">1</button></div>"+
				"<div class=\"ui-block-b\"><button type=\"submit\" data-theme=\"c\">2</button></div>"+
				"<div class=\"ui-block-c\"><button type=\"submit\" data-theme=\"c\">3</button></div>"+
				"</fieldset>"+
				"<div data-role=\"footer\" data-position=\"fixed\" data-theme=\"d\"><h1>Kasetsart University Senior Project </h1></div> "+
				"</body>"+
				"</html>");
		out.close();
	}

}
