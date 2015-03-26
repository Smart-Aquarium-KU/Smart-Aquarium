<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register</title>
</head>
<body>
	     
	<form method="post" name="RForm" action="registerPage">
		   
		<table width="40%"  bgcolor="#efee82" align="center">

			       
			<tr>
				                   
				<td colspan=3><font size=3><center>
							<b>REGISTRATION FORM</b>
						</center></font>            
			</tr>

			       
			<tr>
				           
				<td width="30%">Login Name:<br /></td>            
				<td><input name="username" type="text" size=25 maxlength=20></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Password:<br /></td>            
				<td><input name="userpass" type="password" size=25 maxlength=20></td>
				       
			</tr>
			       
			<tr>
				           
				<td width="30%">Confirm Password:<br /></td>            
				<td><input name="ruserpass" type="password" size=25
					maxlength=20></td>        
			</tr>
			       
			<tr>
				           
				<td width="30%">Email :<br /></td>            
				<td><input name="email" type="text" size=25 maxlength=30></td>
				       
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

</body>
</html>