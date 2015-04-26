
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register</title>
<script>
	//localization strings for the user message find a better way to do it
	var globals = {
		en : {
			usernameMissing : 'Username must be filled out',
			usernameRestriction : 'Username must be be between 3 and 30 characters!',
			usernameRegex : 'Username must be valid!',
			passwordMissing : 'Password must be filled out',
			passwordRestriction : 'Password must be be between 3 and 30 characters!',
			passwordRegex : 'Password must be valid!',
			rpasswordMissing : 'Re-Password must be filled out',
			rpasswordRestriction : 'Re-Password must be be between 3 and 30 characters!',
			rpasswordRegex : 'Re-Password must be valid!',
			emailMissing : 'Email must be filled out',
			emailRestriction : 'Email must be be between 3 and 30 characters!',
			emailRegex : 'Email must be valid!',
			genderMissing : 'Gender must be filled out',
			genderRestriction : 'Gender must be be Female or Male',
			passwordRepasswordConfirmation : 'Password and Re-password must match!'
		}
	};

	function validateForm() {
		//Client side validation 
		//get the form values
		var username = document.forms["registerForm"]["username"].value;
		var userpass = document.forms["registerForm"]["userpass"].value;
		var ruserpass = document.forms["registerForm"]["ruserpass"].value;
		var email = document.forms["registerForm"]["email"].value;
		var gender = document.forms["registerForm"]["gender"].value;

		//regex strings for string validation
		var ck_name = /^[A-Za-z0-9 ]{3,30}$/;

		//regex string for email validation
		var ck_email = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;

		//													USERNAME VALIDATION
		if (username == null || username == "") {
			alert(globals['en'].usernameMissing);
			return false;
		} else if (username.length<3 || username.length>30) {
			alert(globals['en'].usernameRestriction);
			return false;
		}

		if (!ck_name.test(username)) {
			alert(globals['en'].usernameRegex);
			return false;
		}
		//------------------------------------------------------------------------------------------------------------------------------------
		//													PASSWORD VALIDATION
		if (userpass == null || userpass == "") {
			alert(globals['en'].passwordMissing);
			return false;
		} else if (userpass.length<3 || userpass.length>30) {
			alert(globals['en'].passwordRestriction);
			return false;
		}
		//Check for regex
		if (!ck_name.test(userpass)) {
			alert(globals['en'].passwordRegex);
			return false;
		}
		//------------------------------------------------------------------------------------------------------------------------------------

		//													RE-PASSWORD VALIDATION 
		if (ruserpass == null || ruserpass == "") {
			alert(globals['en'].rpasswordMissing);
			return false;
		} else if (ruserpass.length<3 || ruserpass.length>30) {
			alert(globals['en'].rpasswordRestriction);
			return false;
		}
		//Check for regex
		if (!ck_name.test(ruserpass)) {
			alert(globals['en'].rpasswordRegex);
			return false;
		}
		//------------------------------------------------------------------------------------------------------------------------------------
		//													EMAIL VALIDATION
		if (email == null || email == "") {
			alert(globals['en'].emailMissing);
			return false;
		} else if (email.length<3 || email.length>30) {
			alert(globals['en'].emailRestriction);
			return false;
		}

		if (!ck_email.test(email)) {
			alert(globals['en'].emailRegex);
			return false;
		}
		//------------------------------------------------------------------------------------------------------------------------------------
		//													GENDER VALIDATION
		if (gender == null || gender == "") {
			alert(globals['en'].genderMissing);
			return false;
		} else if (gender != 0 && gender != 1) {
			alert(globals['en'].genderRestriction);
			return false;
		}
		//------------------------------------------------------------------------------------------------------------------------------------
		//													PASSWORD/RE-PASSWORD CONFIRMATION
		if(userpass!=ruserpass){
			alert(globals['en'].passwordRepasswordConfirmation);
			return false;
		}
		//------------------------------------------------------------------------------------------------------------------------------------
	}
</script>
</head>
<body>
	     
	<form method="post" name="registerForm" action="registerPage" onsubmit="return validateForm()">
		   
		<table width="40%"  bgcolor="#efee82" align="center">

			       
			<tr>
				                   
				<td colspan=3><font size=3><center>
							<b>REGISTRATION FORM</b>
						</center></font>            
			</tr>

			       
			<tr>
				           
				<td width="30%">Login Name:<br /></td>            
				<td><input name="username" type="text" size=30 maxlength=30></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Password:<br /></td>            
				<td><input name="userpass" type="password" size=30 maxlength=30></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Confirm Password:<br /></td>            
				<td><input name="ruserpass" type="password" size=30
					maxlength=30></td>        
			</tr>
			       
			<tr>
				           
				<td width="30%">Email :<br /></td>            
				<td><input name="email" type="text" size=30 maxlength=30></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30" valign="middle">Gender:</td>              
				<td><select name="gender" onChange="";>              
						   
						<option selected="selected" value="">---Select---</option>        
						         
						<option value="0">Female</option>                  
						<option value="1">Male</option>                
				</select></td>            
			</tr>
			             
			</tr>
			               
			<tr>
				<td><input type="submit" value="Submit">        
			</tr>
		</table>   
	</form>
<div data-role="footer" data-theme="d"><h5>Kasetsart University Senior Project </h5></div> 

</body>
</html>