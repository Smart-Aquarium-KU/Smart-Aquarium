package mete.mertkan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mete.mertkan.objects.Aquarium;
import mete.mertkan.objects.MySQLAccess;


@WebServlet("/addGuest")
public class AddGuestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddGuestServlet() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get the aquarium object from session
		Aquarium aquarium=new Aquarium((Aquarium)request.getSession().getAttribute("aquarium"));

		//get the guest id from user
		int guestId=Integer.parseInt(request.getParameter("guestid"));  

		//This is just a test there are better ways to find user on the database use them later

		//get the database object
		MySQLAccess accessControl=MySQLAccess.getConnection();

		//give permission the spesified
		accessControl.givePermission(guestId, aquarium.getAquarium_id());


	}

}
