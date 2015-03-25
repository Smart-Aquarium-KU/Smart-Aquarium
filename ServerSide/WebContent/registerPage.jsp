<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register</title>
</head>
<body>


	<form action="registerPage" id="register" method="post">
		<table border="0">
			<tbody>
				<tr>
					<td><label for="name">Your Name*: </label></td>
					<td><input id="name" maxlength="45" name="name" type="text" />
					</td>
				</tr>
				<tr>
					<td><label for="email">Email Address*:</label></td>
					<td><input id="email" maxlength="45" name="email" type="text" /></td>
				</tr>
				<tr>
					<td><label for="username">User Name*:</label></td>
					<td><input id="username" maxlength="45" name="username"
						type="text" /></td>
				</tr>
				<tr>
					<td><label for="password">Password*:</label></td>
					<td><input id="password" maxlength="45" name="password"
						type="password" /></td>
				</tr>
				<tr>
					<td align="right"><input name="Submit" type="submit"
						value="Submit" /></td>
				</tr>
			</tbody>
		</table>
	</form>   

	   
	<form method="post" name="RForm" action="registerPage">
		   
		<table width="40%"  bgcolor="#efee82" align="center">

			       
			<tr>
				                   
				<td colspan=3><font size=3><center>
							<b>REGISTRATION FORM</b>
						</center></font>            
			</tr>

			       
			<tr>
				           
				<td width="30%">Login Name:<br /></td>            
				<td><input name="Lname" type="text" size=25 maxlength=20></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Password:<br /></td>            
				<td><input name="Pass" type="password" size=25 maxlength=20></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Confirm Password :<br /></td>            
				<td><input name="RPass" type="password" size=25 maxlength=20></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Email :<br /></td>            
				<td><input name="Email" type="text" size=25 maxlength=20></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30" valign="middle">Gender:</td>              
				<td><select name="Gender" onChange="";>              
						   
						<option selected="selected" value="">---Select---</option>        
						         
						<option value="AU_61">Male</option>                  
						<option value="AU_61">Female</option>                
				</select></td>            
			</tr>
			           
			<td width="30" valign="middle">Select Country :</td>              
			<td><select name="Country" onChange="";>              
					   
					<option selected="selected" value="">---Select---</option>        
					         
					<option value="AU_61">India</option>                  
					<option value="AU_61">Pakistan</option>                  
					<option value="AU_61">Sri Lanka</option>                  
					<option value="AU_61">Nepal</option>                  
					<option value="AU_61">Bangladesh</option>                  
					<option value="AU_61">China</option>                          
			</select></td>            
			</tr>
			       
			<tr>
				           
				<td width="30%">Adress:<br /></td>            
				<td><textarea name="Adress" rows=3 colos=30></textarea></td>        
			</tr>
			       
			<tr>
				           
				<td width="30%">Phone no:<br /></td>            
				<td><input name="Phone" type="text" size=25 maxlength=10></td>
				       
			</tr>
			       
			<tr>
				           
				<td><input type="Reset">            
				<td><input type="submit" value="Submit">        
			</tr>

			   
		</table>
		   
	</form>

</body>
</html>