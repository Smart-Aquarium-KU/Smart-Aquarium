

import java.io.IOException;
import java.io.PrintWriter;

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
		response.setContentType("text/html");  
	    PrintWriter out = response.getWriter();  
	    
	    //This is the most junky way of doing it fix these later!!!
	    String n=request.getParameter("username");  
	    String p=request.getParameter("userpass");  
	    MySQLAccess accessControl= new MySQLAccess();
	    
	    if(accessControl.valideUser(n, p))
	    {
	    	RequestDispatcher rd=request.getRequestDispatcher("mainPage.jsp");  
	         rd.forward(request,response);  
	    }
	    else
	    {
	    	out.print("Sorry username or password incorrect");  
	        RequestDispatcher rd=request.getRequestDispatcher("loginPage.jsp");  
	        rd.include(request,response);  
	    }
		
	}

}
