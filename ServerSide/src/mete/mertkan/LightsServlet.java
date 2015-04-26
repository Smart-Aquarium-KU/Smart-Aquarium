package mete.mertkan;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mete.mertkan.objects.Aquarium;
import mete.mertkan.objects.SendCommand;
import mete.mertkan.objects.User;

@WebServlet("/lights")
public class LightsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int LIGHTSON=1;
	private static final int LIGHTSOFF=2;

	public LightsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//get the user object from session
		User user=new User((User)request.getSession().getAttribute("user"));

		//get the aquarium object from session
		Aquarium aquarium=new Aquarium((Aquarium)request.getSession().getAttribute("aquarium"));

		//command object for sending command
		SendCommand commandSender= new SendCommand();
		
		//encrpt and send.
		commandSender.encryptAndSend(user, LIGHTSON, aquarium);


	}

}
