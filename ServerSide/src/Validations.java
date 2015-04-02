public class Validations {

	private static int SUCCESS = 0;
	private static int FAILURE = 1;
	private static int MISSING_INFO = -1;
	
	//Validation function for strings returns -1 if is null , 1 if its not valid ,0 if its okay
	public int verifyString(String testField)
	{
		testField = testField.trim();

	    if(testField == null || testField.equals(""))
	        return MISSING_INFO;
	    	
	    //this means that it can start with character and continue with number or otherwise or just characters
	    if(!testField.matches("(([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z])|([a-zA-Z]*))"))
	        return FAILURE;

	    return SUCCESS;
	}
	//email validation
	public int verifyEmail(String email)
	{
	    email = email.trim();

	    if(email == null || email.equals(""))
	        return MISSING_INFO;

	    if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"))
	        return FAILURE;

	    return SUCCESS;
	}

}
