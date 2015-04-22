package mete.mertkan;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class lights
 */
@WebServlet("/lights")
public class lights extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public lights() {
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
		
		MySQLAccess accessControl= new MySQLAccess();
		Connection con=null;
		con=accessControl.connect(con);
		
		User user=new User((User)request.getSession().getAttribute("user"));
		Aquarium aquarium=new Aquarium((Aquarium)request.getSession().getAttribute("aquarium"));
		
		SendCommand commandSender= new SendCommand();
		try {
			commandSender.encryptAndSend(user, 1, aquarium);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
