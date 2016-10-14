<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>checkVersion</title>
</head>
<body  onload="myLoad()">
<span id="httpObject" ></span>
<script type="text/javascript">
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
function addDomain()
{
	window.open("http://localhost:61161/addDomain?domain="+window.location.hostname, "AddDomain","height=400, width=400,left=100, top=20");
}
function setOutput(output)
{
	var ret=JSON.parse(output);
	if(ret.ret_code==0x76000031)
	{
		alert(window.location.hostname+"非信任網站，請先加入信任網站");
		document.getElementById('addDomain').disabled="";
	}
	document.getElementById('info').value= JSON.stringify(ret, null, 2);
	var slots = ret.slots;
	var selectSlot = document.getElementById('slotDescription');
	selectSlot.innerHTML="";
	for(var index in slots){ 
		if(slots[index].token instanceof Object){
			var opt = document.createElement('option');
			opt.value = slots[index].slotDescription;
			opt.innerHTML = slots[index].slotDescription+" 卡號:["+slots[index].token.serialNumber+"]";
			selectSlot.appendChild(opt);
		}
	}
}
function getImageInfo(ctx)
{
	var output="";
	for(i=0;i<2000;i++)
	{
		var data=ctx.getImageData(i,0,1,1).data;
		if(data[2]==0) break;
		output=output+String.fromCharCode(data[2],data[1],data[0]);
	}
	if(output=="") output='{"ret_code": 1979711501,"message": "執行檔錯誤或逾時"}';
	return output;
}
function myLoad(){
	var img = null;
	var ctx;
	var output="";
	var ua = window.navigator.userAgent;
	if(ua.indexOf("MSIE")==-1 && ua.indexOf("Trident")==-1) //not IE
	{
		img=document.createElement("img");
		img.crossOrigin = "Anonymous";
		img.src = 'http://localhost:61161/p11Image.bmp';
		var canvas = document.createElement("canvas");
		canvas.width=2000;canvas.height=1;
		ctx = canvas.getContext('2d');
		
		img.onload = function() {
			ctx.drawImage(img, 0, 0);
			output=getImageInfo(ctx);
			setOutput(output);
		};
		img.onerror = function()
		{
			document.getElementById('info').value= "未安裝客戶端程式或未啟動服務";
		};
	}else{
		document.getElementById("httpObject").innerHTML='<OBJECT id="http" width=1 height=1 style="LEFT: 1px; TOP: 1px" type="application/x-httpcomponent" VIEWASTEXT></OBJECT>';
		output=postData("http://localhost:61161/pkcs11info","");
		if(output==null){
			document.getElementById('info').value= "未安裝客戶端程式或未啟動服務";
			return;
		}else setOutput(output);
		
	}
	
}
function receiveMessage(event)
{
	if(event.origin!="http://localhost:61161")
		return;
	try{
		var ret = JSON.parse(event.data);
		if(ret.ret_code==0){
			window.location.reload();
		}else{
			alert("Add domain failed, code="+ret.ret_code);
		}
	}catch(e){
		if(console) console.error(e);
	}
}
if (window.addEventListener) window.addEventListener("message", receiveMessage, false);
</script>
<P>
加入信任網站：<input type="button" name="addDomain" id="addDomain" value="加入" onclick="addDomain()" disabled="disabled"/><br/>
驅動程式資訊：<br/>
<textarea name="info" id="info" rows="10" cols="100"></textarea><br/>
可用讀卡機：
<select name="slotDescription" id="slotDescription"></select>
</P>
</body>
</html>