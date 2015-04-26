package mete.mertkan;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mete.mertkan.objects.MySQLAccess;
import mete.mertkan.objects.User;
import mete.mertkan.objects.Validations;

import java.util.ResourceBundle;

@WebServlet("/registerPage")
public class RegisterPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	int validationString=1;;
	boolean allOk=true;

	public RegisterPageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		//Message Bundle base text
		ResourceBundle message=ResourceBundle.getBundle("MessageBundle");

		//read user input
		String username=request.getParameter("username");  
		String password=request.getParameter("userpass");
		String rePassword=request.getParameter("ruserpass");
		String email=request.getParameter("email");
		int gender=Integer.parseInt(request.getParameter("gender"));

		//Control mechanism for the user input 

		//Server side validations
		Validations validationControl= new Validations();
		//if user input is not valid warn the user
		validationString=validationControl.verifyString(username);
		//Check for username
		if(validationString!=0) //not valid
			allOk=false;

		validationString=validationControl.verifyString(password);
		//check for password
		if(validationString!=0)//not-valid
			allOk=false;

		validationString=validationControl.verifyString(rePassword);
		//check for repassword
		if(validationString!=0)//not-valid
			allOk=false;

		validationString=validationControl.verifyEmail(email);
		//check for email
		if(validationString!=0)//not-valid
			allOk=false;

		//check for both passwords and re-password 
		if(password.compareTo(rePassword)!=0)
			allOk=false;

		if(allOk){

			//User object for database
			User newUser = new User(username,password);
			newUser.setEmail(email);
			newUser.setGender(gender);

			MySQLAccess accessControl= MySQLAccess.getConnection();
		
			accessControl.createUser(newUser);

			RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");  
			rd.include(request,response);  
		}
		else
		{
			RequestDispatcher rd=request.getRequestDispatcher("registerPage.jsp");  
			rd.include(request,response);
			out.print(message.getString("registerError")); 
		}

	}

}
