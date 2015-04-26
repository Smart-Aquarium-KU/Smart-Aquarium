package mete.mertkan;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mete.mertkan.objects.Aquarium;
import mete.mertkan.objects.MySQLAccess;
import mete.mertkan.objects.User;

@WebServlet("/askFish")
public class AddAquariumServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddAquariumServlet() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get the user object from session
		User user=new User((User)request.getSession().getAttribute("user"));

		//get entered strings
		String aquariumName=request.getParameter("aquariumName");
		String macAddress=request.getParameter("macAddress");
		String ipAddress=request.getParameter("ipAddress");

		//there should be validation checks

		//create aquarium object according to user's input
		Aquarium aquarium = new Aquarium(aquariumName, macAddress, ipAddress, user.getId());

		//get the database object
		MySQLAccess accessControl=MySQLAccess.getConnection();

		//create the aquarium and make user's hash word the name of the aquarium
		accessControl.createAquarium(aquarium);
		accessControl.insertHashWord(aquarium.getAquarium_name(), user.getId());

		//redirect to the login page
		RequestDispatcher rd=request.getRequestDispatcher("/loginPage.jsp");
		rd.forward(request,response);

	}

}
