<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>readCertificate</title>
<script type="text/javascript" src="<c:url value="/resources/js/errorcode.js"/>"></script>
<SCRIPT type="text/javascript">
var postTarget;
var timeoutId;
function postData(target,data)
{
	if(!http.sendRequest)
	{
		return null;
	}
	http.url=target;
	http.actionMethod="POST";
	var code=http.sendRequest(data);
	if(code!=0) return null;
	return http.responseText;

}
function checkFinish(){
	if(postTarget){
		postTarget.close();
		alert("尚未安裝元件");
	}
}
function getUserCert()
{
    var ua = window.navigator.userAgent;
	if(ua.indexOf("MSIE")!=-1 || ua.indexOf("Trident")!=-1) //is IE, use ActiveX
	{
		postTarget=window.open("http://localhost:61161/waiting.gif", "Reading","height=200, width=200, left=100, top=20");
		document.getElementById("httpObject").innerHTML='<OBJECT id="http" width=1 height=1 style="LEFT: 1px; TOP: 1px" type="application/x-httpcomponent" VIEWASTEXT></OBJECT>';
		var data=postData("http://localhost:61161/pkcs11info?withcert=true","");
		postTarget.close();
		postTarget=null;
		if(!data)
			alert("尚未安裝元件");
		else
			setUserCert(data);
	}
	else{
		postTarget=window.open("http://localhost:61161/popupForm", "憑證讀取中","height=200, width=200, left=100, top=20");
		timeoutId=setTimeout(checkFinish,3500);
	}
}

function setUserCert(certData)
{
	var ret=JSON.parse(certData);
	document.getElementById("returnCode").value=ret.ret_code;
	if(ret.ret_code!=0){
		alert(MajorErrorReason(ret.ret_code));
		if(ret.last_error)
			alert(MinorErrorReason(ret.last_error));
		return;
	}
	//var usage="keyEncipherment|dataEncipherment";
	var usage="digitalSignature";
	var slots = ret.slots;
	for(var index in slots){
		if(slots[index].token==null || slots[index].token==="unknown token") continue;
		var certs=slots[index].token.certs;
		for(var indexCert in certs){
			//console.log("indexCert: " + indexCert);
			//console.log("usage: " + certs[indexCert].usage);
			if(certs[indexCert].usage==usage){
				document.getElementById("certb64").value=certs[indexCert].certb64;
				return;
			}
		}
	}
	alert("找不到可用來加密的憑證");
}

function receiveMessage(event)
{
	if(console)
		console.debug(event);
	
	//安全起見，這邊應填入網站位址檢查
	if(event.origin!="http://localhost:61161")
		return;
	try{
		var ret = JSON.parse(event.data);
		console.debug(ret);
		if(ret.func){
			if(ret.func=="getTbs"){
				clearTimeout(timeoutId);
				var tbsData = {"func":"GetUserCert"};
				var json=JSON.stringify(tbsData);
				postTarget.postMessage(json,"*");
			}else if(ret.func=="pkcs11info"){
				setUserCert(event.data);
			}
		}else{
			if(console) console.error("no func");
		}
	}catch(e){
		//errorhandle
		if(console) console.error(e);
	}
}
if (window.addEventListener) {
	window.addEventListener("message", receiveMessage, false);
	}else {
	//for IE8
		window.attachEvent("onmessage", receiveMessage);
	}
	//for IE8
var console=console||{"log":function(){}, "debug":function(){}, "error":function(){}};
</SCRIPT> 
</head>
<body>
<span id="httpObject" ></span>
<H1>PKCS#7加密範例</H1><br/>
1.<INPUT type="button" name="button1" value="讀取憑證" onclick="getUserCert()"> <br/>
<form:form name="DemoForm" action="decryptData.do" method="POST"> 
<TEXTAREA name="certb64" id="certb64" rows="8" cols="65"></TEXTAREA><br/>
plain data:<INPUT name="plainData" value="To Be Encrypt"><BR>
cipher type:<select name="cipherType"><option value="PKCS7" selected>PKCS7</option><option value="PKCS1">PKCS1</option></select><br/>
plain encode type:<select name="plainEncoding"><option value="none" selected>NONE</option><option value="base64">base64</option></select><br/>
2.<INPUT type="submit" name="button2" value="加密資料" /> <br/>
</form:form>
錯誤代碼：<input type="text" name="returnCode" id="returnCode"/>
<a href="<c:url value="/logout" />">Logout</a>
</body>
</html>