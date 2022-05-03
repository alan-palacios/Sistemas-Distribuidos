/*
  Servicio.java
  Servicio web tipo REST
  Alan Yoltic Palacios Lugo
*/

package negocio;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.ArrayList;
import com.google.gson.*;

// la URL del servicio web es http://localhost:8080/Servicio/rest/ws
// donde:
//	"Servicio" es el dominio del servicio web (es decir, el nombre de archivo Servicio.war)
//	"rest" se define en la etiqueta <url-pattern> de <servlet-mapping> en el archivo WEB-INF\web.xml
//	"ws" se define en la siguiente anotacin @Path de la clase Servicio

@Path("ws")
public class Servicio
{
  static DataSource pool = null;
  static
  {		
    try
    {
      Context ctx = new InitialContext();
      pool = (DataSource)ctx.lookup("java:comp/env/jdbc/datasource_Servicio");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  static Gson j = new GsonBuilder()
		.registerTypeAdapter(byte[].class,new AdaptadorGsonBase64())
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
		.create();

  @POST
  @Path("alta_articulo")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response alta(@FormParam("articulo")  Articulo articulo) throws Exception
  {
    Connection conexion = pool.getConnection();

    if (articulo.descripcion == null || articulo.descripcion.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la descripción"))).header("Access-Control-Allow-Origin", "*").build();

    if (articulo.precio == null || articulo.precio<1)
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar el precio"))).header("Access-Control-Allow-Origin", "*").build();

    if (articulo.cantidad_almacen == null || articulo.cantidad_almacen<1)
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar la cantidad disponible en almacen"))).header("Access-Control-Allow-Origin", "*").build();

    if (articulo.foto == null || articulo.foto.equals(""))
      return Response.status(400).entity(j.toJson(new Error("Se debe ingresar una foto"))).header("Access-Control-Allow-Origin", "*").build();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT id_articulo FROM articulos WHERE descripcion=?");
      try
      {
        stmt_1.setString(1,articulo.descripcion);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          if (rs.next())
             return Response.status(400).entity(j.toJson(new Error("El articulo ya existe"))).header("Access-Control-Allow-Origin", "*").build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }

      PreparedStatement stmt_2 = conexion.prepareStatement("INSERT INTO articulos VALUES (0,?,?,?)");
      try
      {
        stmt_2.setString(1,articulo.descripcion);
        stmt_2.setInt(2,articulo.precio);
        stmt_2.setInt(3,articulo.cantidad_almacen);
        stmt_2.executeUpdate();
      }
      finally
      {
        stmt_2.close();
      }

      if (articulo.foto != null)
      {
        PreparedStatement stmt_3 = conexion.prepareStatement("INSERT INTO fotos_articulos VALUES (0,?,(SELECT id_articulo FROM articulos WHERE descripcion=?))");
        try
        {
          stmt_3.setBytes(1,articulo.foto);
          stmt_3.setString(2,articulo.descripcion);
          stmt_3.executeUpdate();
        }
        finally
        {
          stmt_3.close();
        }
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }
    finally
    {
      conexion.close();
    }
    return Response.ok().header("Access-Control-Allow-Origin", "*").build();
  }

  @POST
  @Path("consulta_articulo")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consulta(@FormParam("id_articulo") String id_articulo) throws Exception
  {
    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT a.descripcion, a.precio,a.cantidad_almacen, b.foto, a.id_articulo FROM articulos a LEFT OUTER JOIN fotos_articulos b ON a.id_articulo=b.id_articulo WHERE a.id_articulo=?");
      try
      {
        stmt_1.setString(1,id_articulo);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          if (rs.next())
          {
            Articulo r = new Articulo();
            r.descripcion = rs.getString(1);
            r.precio = rs.getInt(2);
            r.cantidad_almacen = rs.getInt(3);
	          r.foto = rs.getBytes(4);
	          r.id_articulo = rs.getInt(5);
            return Response.ok().entity(j.toJson(r)).header("Access-Control-Allow-Origin", "*").build();
          }
          return Response.status(400).entity(j.toJson(new Error("El articulo no existe"))).header("Access-Control-Allow-Origin", "*").build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }
    finally
    {
      conexion.close();
    }
  }

  @POST
  @Path("buscar_articulos")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response busqueda(@FormParam("descripcion") String descripcion) throws Exception
  {
    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT  a.descripcion, a.precio,a.cantidad_almacen, b.foto, a.id_articulo FROM articulos a LEFT OUTER JOIN fotos_articulos b ON a.id_articulo=b.id_articulo WHERE a.descripcion LIKE CONCAT( '%',?,'%')");
      try
      {
        stmt_1.setString(1,descripcion);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          ArrayList<Articulo> articulos = new ArrayList<>();
          while(rs.next())
          {
            Articulo r = new Articulo();
            r.descripcion = rs.getString(1);
            r.precio = rs.getInt(2);
            r.cantidad_almacen = rs.getInt(3);
	          r.foto = rs.getBytes(4);
	          r.id_articulo = rs.getInt(5);
            articulos.add(r);
          }
          if(articulos!=null && articulos.size()>0) return Response.ok().entity(j.toJson(articulos)).header("Access-Control-Allow-Origin", "*").build();
          return Response.status(400).entity(j.toJson(new Error("El articulo no existe"))).header("Access-Control-Allow-Origin", "*").build();
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }
    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }
    finally
    {
      conexion.close();
    }
  }

  @POST
  @Path("agregar_carrito")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response agregar_carrito(@FormParam("descripcion") String descripcion, @FormParam("cantidad") Integer cantidad) throws Exception
  {
    Connection conexion= pool.getConnection();
    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT id_articulo, precio, cantidad_almacen FROM articulos WHERE descripcion=?");
      try
      {
        stmt_1.setString(1, descripcion);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          if (rs.next())
          {
            //transaccion
            conexion.setAutoCommit(false);
            try {
              Integer id_articulo=rs.getInt(1);
              Integer precio=rs.getInt(2);
              Integer cantidad_almacen=rs.getInt(3);
              if (cantidad>0 && cantidad<=cantidad_almacen)
              {
                PreparedStatement stmt_3 = conexion.prepareStatement("INSERT INTO carrito_compra VALUES (?,?)");
                try
                {
                  stmt_3.setInt(1, id_articulo);
                  stmt_3.setInt(2,cantidad);
                  stmt_3.executeUpdate();
                }
                finally
                {
                  stmt_3.close();
                }
                PreparedStatement stmt_4 = conexion.prepareStatement("UPDATE articulos SET cantidad_almacen=? WHERE id_articulo=?");
                try
                {
                  stmt_4.setInt(1, cantidad_almacen-cantidad);
                  stmt_4.setInt(2, id_articulo);
                  stmt_4.executeUpdate();
                }
                finally
                {
                  stmt_4.close();
                }
              }
              conexion.commit();
              
            } catch(SQLException e) {
              // in case of exception, rollback the transaction
              conexion.rollback();
              return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
            } finally{
              conexion.setAutoCommit(true);
            }
          }else{
            return Response.status(400).entity(j.toJson(new Error("El articulo no existe"))).header("Access-Control-Allow-Origin", "*").build();
          }
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }

    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }
    finally
    {
      conexion.close();
    }
    return Response.ok().header("Access-Control-Allow-Origin", "*").build();
  }

  @POST
  @Path("consulta_carrito")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response consulta_carrito() throws Exception
  {
    Connection conexion= pool.getConnection();

    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT a.descripcion, a.precio, b.foto, c.cantidad, a.id_articulo FROM carrito_compra c INNER JOIN articulos a ON c.id_articulo=a.id_articulo INNER JOIN fotos_articulos b ON c.id_articulo=b.id_articulo");
      try
      {
        ResultSet rs = stmt_1.executeQuery();
          ArrayList<Articulo> articulos = new ArrayList<>();
          while(rs.next())
          {
            Articulo r = new Articulo();
            r.descripcion = rs.getString(1);
            r.precio = rs.getInt(2);
	          r.foto = rs.getBytes(3);
            r.cantidad = rs.getInt(4);
            r.id_articulo = rs.getInt(5);
            articulos.add(r);
          }
          return Response.ok().entity(j.toJson(articulos)).header("Access-Control-Allow-Origin", "*").build();
      }catch (Exception e){
          return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
      }finally{
        stmt_1.close();
      }
    }catch (Exception e){
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }finally{
      conexion.close();
    }
  }

  @POST
  @Path("borra_articulo_carrito")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response borra_articulo_carrito(@FormParam("id_articulo") Integer id_articulo) throws Exception
  {
    Connection conexion= pool.getConnection();
    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT id_articulo, cantidad FROM carrito_compra WHERE id_articulo=?");
      try
      {
        stmt_1.setInt(1, id_articulo);

        ResultSet rs = stmt_1.executeQuery();
        try
        {
          if (rs.next())
          {
            //transaccion
            conexion.setAutoCommit(false);
            try {
              Integer cantidad=rs.getInt(2);
              PreparedStatement stmt_3 = conexion.prepareStatement("DELETE FROM carrito_compra where id_articulo=?");
              try
              {
                stmt_3.setInt(1, id_articulo);
                stmt_3.executeUpdate();
              }
              finally
              {
                stmt_3.close();
              }
              PreparedStatement stmt_4 = conexion.prepareStatement("UPDATE articulos SET cantidad_almacen=cantidad_almacen+? WHERE id_articulo=?");
              try
              {
                stmt_4.setInt(1, cantidad);
                stmt_4.setInt(2, id_articulo);
                stmt_4.executeUpdate();
              }
              finally
              {
                stmt_4.close();
              }
              conexion.commit();
              
            } catch(SQLException e) {
              // in case of exception, rollback the transaction
              conexion.rollback();
              return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
            } finally{
              conexion.setAutoCommit(true);
            }
          }else{
            return Response.status(400).entity(j.toJson(new Error("El articulo no existe"))).header("Access-Control-Allow-Origin", "*").build();
          }
        }
        finally
        {
          rs.close();
        }
      }
      finally
      {
        stmt_1.close();
      }

    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }
    finally
    {
      conexion.close();
    }
    return Response.ok().header("Access-Control-Allow-Origin", "*").build();
  }

  @POST
  @Path("borra_carrito")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response borra_carrito() throws Exception
  {
    Connection conexion= pool.getConnection();
    try
    {
      PreparedStatement stmt_1 = conexion.prepareStatement("SELECT a.descripcion, a.precio, b.foto, c.cantidad, a.id_articulo FROM carrito_compra c INNER JOIN articulos a ON c.id_articulo=a.id_articulo INNER JOIN fotos_articulos b ON c.id_articulo=b.id_articulo");
      try
      {
        ResultSet rs = stmt_1.executeQuery();
          ArrayList<Articulo> articulos = new ArrayList<>();
          while(rs.next())
          {
            Articulo r = new Articulo();
            r.descripcion = rs.getString(1);
            r.precio = rs.getInt(2);
	          r.foto = rs.getBytes(3);
            r.cantidad = rs.getInt(4);
            r.id_articulo = rs.getInt(5);
            articulos.add(r);
          }
            //transaccion
            conexion.setAutoCommit(false);
            try {
                PreparedStatement stmt_3 = conexion.prepareStatement("DELETE FROM carrito_compra");
                try
                {
                  stmt_3.executeUpdate();
                }
                finally
                {
                  stmt_3.close();
                }
              for(Articulo articulo: articulos){
                PreparedStatement stmt_4 = conexion.prepareStatement("UPDATE articulos SET cantidad_almacen=cantidad_almacen+? WHERE id_articulo=?");
                try
                {
                  stmt_4.setInt(1, articulo.cantidad);
                  stmt_4.setInt(2, articulo.id_articulo);
                  stmt_4.executeUpdate();
                }
                finally
                {
                  stmt_4.close();
                }
                conexion.commit();
              }
              
            } catch(SQLException e) {
              // in case of exception, rollback the transaction
              conexion.rollback();
              return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
            } finally{
              conexion.setAutoCommit(true);
            }
      }
      finally
      {
        stmt_1.close();
      }

    }
    catch (Exception e)
    {
      return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).header("Access-Control-Allow-Origin", "*").build();
    }
    finally
    {
      conexion.close();
    }
    return Response.ok().header("Access-Control-Allow-Origin", "*").build();
  }
}
