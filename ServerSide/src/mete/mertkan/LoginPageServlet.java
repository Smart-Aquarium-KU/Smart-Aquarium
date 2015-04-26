package mete.mertkan;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import mete.mertkan.objects.MySQLAccess;
import mete.mertkan.objects.User;
import mete.mertkan.objects.Validations;


@WebServlet("/loginPage")
public class LoginPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//User types
	private static final int NORMALUSER = 1;
	private static final int ADMINUSER = 2;
	private static final int GUESSUSER = 3;
	
	//validation control integer if its 0-Success 1-failure -1-missing_info
	int validationString=1;
	boolean allOk=true;

	public LoginPageServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Message Bundle base text
		ResourceBundle message=ResourceBundle.getBundle("MessageBundle");

	

		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  

		//Get the username and password from loginPage.jsp
		String username=request.getParameter("username");  
		String password=request.getParameter("userpass");

		//database object
		MySQLAccess accessControl= MySQLAccess.getConnection();

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

		if(allOk){
			if(accessControl.authenticate(username, password)){

				User user=accessControl.readUsersInfo( username);

				//re-direct other page with user object
				if(NORMALUSER==user.getRoleId()){
					request.getSession().setAttribute("user", user);
					RequestDispatcher rd=request.getRequestDispatcher("/userPanel");
					rd.forward(request,response);
				}
				else if(ADMINUSER==user.getRoleId()){
					//not designed yet
					request.getRequestDispatcher("adminPanel").forward(request,response);  
				}
				else if(GUESSUSER==user.getRoleId()){
					request.getSession().setAttribute("user", user);
					RequestDispatcher rd=request.getRequestDispatcher("/guestPanel");
					rd.forward(request,response);  
				}
			}else{
				out.print(message.getString("incorrectUorP"));  
				RequestDispatcher rd=request.getRequestDispatcher("/");  
				rd.include(request,response);  
			}
		}
		else{
			out.print(message.getString("notValid"));  
			RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");  
			rd.include(request,response);  
		}
		out.close();
	}
}
