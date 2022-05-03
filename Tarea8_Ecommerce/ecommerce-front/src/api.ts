import axios from "axios";

const URL = "http://20.231.216.213/Servicio/rest/ws"
const post = (route: string, data: any, onSuccess: (code: any, res:any)=>void)=>{
	var request = new XMLHttpRequest();
	var body = "";
	var pairs = [];
	var name;
	try
	{
		for (name in data)
		{
			var value = data[name];
			if (typeof value !== "string") value = JSON.stringify(value);
			pairs.push(name + '=' + encodeURI(value).replace(/=/g,'%3D').replace(/&/g,'%26').replace(/%20/g,'+'));
		}
		body = pairs.join('&');
		request.open('POST', `${URL}/${route}`);
		request.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
		//request.setRequestHeader('Content-Length', body.length.toString());
		request.responseType = 'json';
		request.addEventListener("load", (e)=>
		{
			if (onSuccess != null) onSuccess(request.status, request.response);
		})
		request.send(body);
	}
	catch (e:any)
	{
		alert("Error: " + e.message);
	}
	/*
	return await axios.post(`${URL}/alta_articulo`, data).then(res=>{
		console.log(res);
	});
	*/
}
/*
javac -cp /home/alan/apache-tomcat-8.5.78/lib/javax.ws.rs-api-2.0.1.jar:/home/alan/apache-tomcat-8.5.78/lib/gson-2.3.1.jar:. negocio/Servicio.java
rm WEB-INF/classes/negocio/*
cp negocio/*.class WEB-INF/classes/negocio/.
jar cvf Servicio.war WEB-INF META-INF
rm ~/apache-tomcat-8.5.78/webapps/Servicio.war ~/apache-tomcat-8.5.78/webapps/Servicio
ls ~/apache-tomcat-8.5.78/webapps
mv -f ~/Servicio/Servicio.war ~/apache-tomcat-8.5.78/webapps/.
*/

export default {
	post
}