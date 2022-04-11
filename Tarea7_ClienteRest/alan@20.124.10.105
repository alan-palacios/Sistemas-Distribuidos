import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class ClienteRest {
	static Scanner reader = new Scanner(System.in);
	static String baseURL = "http://20.124.10.105/Servicio/rest/ws/";
	static GsonBuilder gsonBuilder = new GsonBuilder();
	//gsonBuilder.serializeNulls();
	static Gson gson = gsonBuilder.create();
	public static void main(String[] args) {
		char seleccion='e';
		while (seleccion!='d') {
			System.out.println("MENU");
			System.out.println("a. Alta usuario");
			System.out.println("b. Consulta usuario");
			System.out.println("c. Borra usuario");
			System.out.println("d. Salir");
			System.out.print("Opci\u00F3n: ");
			seleccion = reader.next().charAt(0);
			switch (seleccion) {
				case 'a':
					altaUsuario();		
					break;
				case 'b':
					consultaUsuario();		
					break;
				case 'c':
					borraUsuario();		
					break;
				default:
					break;
			}
		} 
	}	
	public static void borraUsuario() {
		System.out.print("Ingresa el email: ");
		String email = reader.next();
		try {
			String parametros = "email="+URLEncoder.encode(email, "UTF-8");
			HttpURLConnection conexion = sendHTTP(baseURL+"borra_usuario", parametros);
			// se debe verificar si hubo error 
			int responseCode= conexion.getResponseCode();
			if (responseCode== 200) {
				System.out.println("OK");
			} else {
				System.out.println("ERROR");
				// hubo error 
				BufferedReader br = new BufferedReader(new InputStreamReader((conexion .getErrorStream())));
				String respuesta=""; 
				String aux; 
				// el método web regresa una instancia de la clase Error en formato JSON 
				while ( (aux = br. readLine()) != null) respuesta+=aux;
				Error error = gson.fromJson(respuesta, Error.class);
				System.out.println(error.message);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void consultaUsuario() {
		System.out.print("Ingresa el email: ");
		String email = reader.next();
		try {
			String parametros = "email="+URLEncoder.encode(email, "UTF-8");
			HttpURLConnection conexion = sendHTTP(baseURL+"consulta_usuario", parametros);
			// se debe verificar si hubo error 
			int responseCode= conexion.getResponseCode();
			if (responseCode== 200) {
				System.out.println("OK");
				BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
				String respuesta=""; 
				String aux; 
				// el método web regresa una instancia de la clase Error en formato JSON 
				while ( (aux = br. readLine()) != null) respuesta+=aux;
				Usuario usuario = gson.fromJson(respuesta, Usuario.class);
				System.out.println("Usuario encontrado:");
				System.out.println(usuario);
				System.out.println("\u00BFDesea modificar el usuario (s/n)?");
				char res = reader.next().charAt(0);
				if(res=='s' || res=='S'){
					modificaUsuario(usuario);
				}
				
			} else {
				System.out.println("ERROR");
				// hubo error 
				BufferedReader br = new BufferedReader(new InputStreamReader((conexion .getErrorStream())));
				String respuesta=""; 
				String aux; 
				// el método web regresa una instancia de la clase Error en formato JSON 
				while ( (aux = br. readLine()) != null) respuesta+=aux;
				Error error = gson.fromJson(respuesta, Error.class);
				System.out.println(error.message);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void modificaUsuario(Usuario usuario) {
		System.out.print("Ingresa el nombre ("+usuario.nombre+"): ");
		reader.nextLine();
		String nombre = reader.nextLine();
		if(!nombre.equals("")) usuario.nombre=nombre;

		System.out.print("Ingresa el apellido paterno("+usuario.apellido_paterno+"): ");
		String apellido_paterno = reader.nextLine();
		if(!apellido_paterno.equals("")) usuario.apellido_paterno=apellido_paterno;

		System.out.print("Ingresa el apellido materno("+usuario.apellido_materno+"): ");
		String apellido_materno = reader.nextLine();
		if(!apellido_materno.equals("")) usuario.apellido_materno=apellido_materno;

		System.out.print("Ingresa la fecha de nacimiento ("+usuario.fecha_nacimiento+"): ");
		String fecha_nacimiento = reader.nextLine();
		if(!fecha_nacimiento.equals("")) usuario.fecha_nacimiento=fecha_nacimiento;

		System.out.print("Ingresa el tel\u00E9fono ("+usuario.telefono+"): ");
		String telefono = reader.nextLine();
		if(!telefono.equals("")) usuario.telefono=telefono;

		System.out.print("Ingresa el g\u00E9nero ("+usuario.genero+"): ");
		String genero = reader.nextLine().substring(0,0);
		if(!genero.equals("")) usuario.genero=genero.charAt(0);
		String usuarioJson = gson.toJson(usuario);
		try {
			String parametros = "usuario="+URLEncoder.encode(usuarioJson, "UTF-8");
			HttpURLConnection conexion = sendHTTP(baseURL+"modifica_usuario", parametros);
			// se debe verificar si hubo error 
			int responseCode= conexion.getResponseCode();
			if (responseCode== 200) {
				System.out.println("OK");
			} else {
				System.out.println("ERROR");
				// hubo error 
				BufferedReader br = new BufferedReader(new InputStreamReader((conexion .getErrorStream())));
				String respuesta=""; 
				String aux; 
				// el método web regresa una instancia de la clase Error en formato JSON 
				while ( (aux = br. readLine()) != null) respuesta+=aux;
				Error error = gson.fromJson(respuesta, Error.class);
				System.out.println(error.message);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void altaUsuario() {
		System.out.print("Ingresa el email: ");
		String email = reader.next();
		System.out.print("Ingresa el nombre: ");
		String nombre = reader.next();
		System.out.print("Ingresa el apellido paterno: ");
		String apellido_paterno = reader.next();
		System.out.print("Ingresa el apellido materno: ");
		String apellido_materno = reader.next();
		System.out.print("Ingresa la fecha de nacimiento (AAAA-MM-DD): ");
		String fecha_nacimiento = reader.next();
		System.out.print("Ingresa el tel\u00E9fono: ");
		String telefono = reader.next();
		System.out.print("Ingresa el g\u00E9nero (M/F): ");
		char genero = reader.next().charAt(0);
		Usuario usuario = new Usuario(email, nombre, apellido_paterno, apellido_materno, fecha_nacimiento, telefono, genero);
		String usuarioJson = gson.toJson(usuario);
		try {
			String parametros = "usuario="+URLEncoder.encode(usuarioJson, "UTF-8");
			HttpURLConnection conexion = sendHTTP(baseURL+"alta_usuario", parametros);
			// se debe verificar si hubo error 
			int responseCode= conexion.getResponseCode();
			if (responseCode== 200) {
				System.out.println("OK");
			} else {
				System.out.println("ERROR");
				// hubo error 
				BufferedReader br = new BufferedReader(new InputStreamReader((conexion .getErrorStream())));
				String respuesta=""; 
				String aux; 
				// el método web regresa una instancia de la clase Error en formato JSON 
				while ( (aux = br. readLine()) != null) respuesta+=aux;
				Error error = gson.fromJson(respuesta, Error.class);
				System.out.println(error.message);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static HttpURLConnection sendHTTP(String urlString, String parametros) {
		HttpURLConnection conexion= null;
		try {
			URL url = new URL(urlString);	
			conexion = (HttpURLConnection) url.openConnection();
			// true si se va a enviar un  "body", en este caso el "body son los parámetros"
			conexion.setDoOutput (true); 
			// en este caso utilizamos el método POST de HTTP 
			conexion.setRequestMethod ( "POST" );
			// indica que la petición estará codificada como URL 
			conexion.setRequestProperty ( "Content-Type" , "application/x-www-form-urlencoded" );
			conexion.setRequestProperty( "charset", "utf-8");
			
			// el método web "consulta usuario" recibe como parámetro el email de un usuario, en este caso el email es a@c
			OutputStream os = conexion.getOutputStream(); 
			if(parametros!=null){
				os.write(parametros.getBytes()); 
			}
			os.flush(); 
			return conexion;
		} catch (Exception e) {
			System.out.println(e);
			return conexion;
		} finally{
			if(conexion!=null)
				conexion.disconnect();
		}
	}

	static class Error {
		String message;
		public Error(String message){
			this.message=message;
		}
	}
	static class Usuario {
		String email;
		String nombre;
		String apellido_paterno;
		String apellido_materno;
		String fecha_nacimiento;
		String telefono;
		char genero;
		String foto = null;

		public Usuario(String email, String nombre, String apellidoPat, String apellidoMat, String fecha_nacimiento, String telefono,	char genero){
			this.email = email;
			this.nombre = nombre;
			this.apellido_paterno = apellidoPat;
			this.apellido_materno = apellidoMat;
			this.fecha_nacimiento = fecha_nacimiento;
			this.telefono = telefono;
			this.genero = genero;
		}
		
		@Override
		public String toString() {
			return "email: "+email+"\nnombre: "+nombre+
				"\napellido paterno: "+apellido_paterno+
				"\napellido materno: "+apellido_materno+
				"\nfecha de nacimiento: "+fecha_nacimiento+
				"\ntel\u00E9fono: "+telefono+
				"\ng\u00E9nero: "+genero;
		}                          
	}                              
}                                  
                                   
                                   