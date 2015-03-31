

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class loginPage
 */
@WebServlet("/loginPage")
public class loginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//User types
	private static int NORMALUSER = 1;
	private static int ADMINUSER = 2;
	private static int GUESSUSER = 3;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public loginPage() {
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

		//Message Bundle base text
		ResourceBundle message=ResourceBundle.getBundle("MessageBundle");

		//validation control integer if its 0-Success 1-failure -1-missing_info
		int validationString;
		boolean allOk=true;

		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  

		//Get the username and password from loginPage.jsp
		String n=request.getParameter("username");  
		String p=request.getParameter("userpass");

		//database object
		MySQLAccess accessControl= new MySQLAccess();

		//Server side validations
		Validations validationControl= new Validations();
		
		

		//if user input is not valid warn the user
		validationString=validationControl.verifyString(n);
		//Check for username
		if(validationString!=0) //not valid
			allOk=false;
		

		validationString=validationControl.verifyString(p);
		//check for password
		if(validationString!=0)//not-valid
			allOk=false;
		
		if(allOk){
			
			//User object
			User newUser = new User(n,p);

			if(NORMALUSER==accessControl.valideUser(newUser)){
				RequestDispatcher rd=request.getRequestDispatcher("userPanel.jsp");  
				rd.forward(request,response);  
			}
			else if(ADMINUSER==accessControl.valideUser(newUser)){
				RequestDispatcher rd=request.getRequestDispatcher("adminPanel.jsp");  
				rd.forward(request,response);  
			}
			else if(GUESSUSER==accessControl.valideUser(newUser)){
				RequestDispatcher rd=request.getRequestDispatcher("guessPanel.jsp");  
				rd.forward(request,response);  
			}
			else{
				out.print(message.getString("incorrectUorP"));  
				RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");  
				rd.include(request,response);  
			}
		}
		else
		{
			out.print(message.getString("notValid"));  
			RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");  
			rd.include(request,response);  
		}

	}

}
