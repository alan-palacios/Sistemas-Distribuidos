/*
  Alan Yoltic Palacios Lugo
*/

package negocio;

import com.google.gson.*;

public class Articulo
{
  Integer id_articulo;
  String descripcion;
  Integer precio;
  Integer cantidad_almacen;
  Integer cantidad;
  byte[] foto;

  // @FormParam necesita un metodo que convierta una String al objeto de tipo Usuario
  public static Articulo valueOf(String s) throws Exception
  {
    Gson j = new GsonBuilder().registerTypeAdapter(byte[].class,new AdaptadorGsonBase64()).create();
    return (Articulo)j.fromJson(s,Articulo.class);
  }
}
