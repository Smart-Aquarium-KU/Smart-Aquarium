<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Smart Aquarium</title>
</head>
<body>


<div id="button" style="font:14pt Arial; color:#008080;"></div>

<button onclick="location.href='loginPage.jsp'">Login</button>
<button onclick="location.href='registerPage.jsp'">Create an account</button> 
<table align="center"> 
<tr><td> </td></tr> 
<tr><td> </td></tr> 
<tr><td> </td></tr> 
<tr><td> </td></tr> 
<tr><td> </td></tr> 
<tr><td> </td></tr> 
<tr><td align="center"> Smart Aquarium </td></tr> 
<tr><td> </td></tr> 
<tr><td align="center"> Be smarter than your fish trust the Smart Aquarium.<br>You don't need to worry about pet fish, Smart Aquarium will do this for you. <br/> All you need to do is enjoying them.
 </td></tr> 
</td></tr> 
</table>


<div id="clockbox" style="font:14pt Arial; color:#008080;"></div>
<script type="text/javascript">

tday=new Array("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");
tmonth=new Array("January","February","March","April","May","June","July","August","September","October","November","December");

function GetClock(){
var d=new Date();
var nday=d.getDay(),nmonth=d.getMonth(),ndate=d.getDate(),nyear=d.getYear(),nhour=d.getHours(),nmin=d.getMinutes(),nsec=d.getSeconds(),ap;

     if(nhour==0){ap=" AM";nhour=12;}
else if(nhour<12){ap=" AM";}
else if(nhour==12){ap=" PM";}
else if(nhour>12){ap=" PM";nhour-=12;}

if(nyear<1000) nyear+=1900;
if(nmin<=9) nmin="0"+nmin;
if(nsec<=9) nsec="0"+nsec;

document.getElementById('clockbox').innerHTML=""+tday[nday]+", "+tmonth[nmonth]+" "+ndate+", "+nyear+" "+nhour+":"+nmin+":"+nsec+ap+"";
}

window.onload=function(){
GetClock();
setInterval(GetClock,1000);
}
</script>
<div id="clockbox"></div>

</body>
</html>