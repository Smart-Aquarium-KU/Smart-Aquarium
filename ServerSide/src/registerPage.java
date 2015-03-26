

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class registerPage
 */
@WebServlet("/registerPage")
public class registerPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registerPage() {
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
	    
	    
	    String n=request.getParameter("username");  
	    String p=request.getParameter("userpass");
	    String rp=request.getParameter("ruserpass");
	    String e=request.getParameter("email");
	    int g=Integer.parseInt(request.getParameter("gender"));
	    
	    //Control mechanism for the user input 
	    
	    //After controlling we can put data into user object
	    User newUser=new User();
	    newUser.setUsername(n);
	    newUser.setUserpass(p);
	    newUser.setEmail(e);
	    newUser.setGender(g);
	    
	    //write the user information to database
	    
	    
	}

}
