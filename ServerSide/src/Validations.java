import java.util.regex.*;
public class Validations {

	private static int SUCCESS = 0;
	private static int FAILURE = 1;
	private static int MISSING_INFO = -1;
	public static String ALL_STRING_REGEX = "[\\w]+";
	public static String ALL_NUMBER_REGEX = "[0-9]+";

	// If it is null, then a missing info return code is returned (-1)
	public int testFieldValue(String regexString, String strFieldInput){
		int retCode = FAILURE;
		if ((regexString != null) && (regexString.length() > 0) &&
				(strFieldInput != null) && (strFieldInput.length() > 0))
		{
			Pattern pattern = Pattern.compile(regexString);
			Matcher matcher = pattern.matcher(strFieldInput);
			if (matcher.find()) 
			{
				retCode = SUCCESS;
			} 
		}
		else
		{
			retCode = MISSING_INFO;
		}
		return retCode;
	}
	
	public int checkForString(String inputtedValue){
		int retCode = FAILURE;
        if ((inputtedValue != null) && (inputtedValue.length() > 0))
            {
            Pattern pattern = Pattern.compile(ALL_STRING_REGEX);
            Matcher matcher = pattern.matcher(inputtedValue);
            if (matcher.find()) 
                {
                retCode = SUCCESS;
            }
        }
        return retCode;
	}
	
	public int checkForNumber(String inputtedValue){
		int retCode = FAILURE;
        if ((inputtedValue != null) && (inputtedValue.length() > 0))
            {
            Pattern pattern = Pattern.compile(ALL_NUMBER_REGEX);
            Matcher matcher = pattern.matcher(inputtedValue);
            if (matcher.find()) 
                {
                retCode = SUCCESS;
            }
        }
        return retCode;
	}

}
