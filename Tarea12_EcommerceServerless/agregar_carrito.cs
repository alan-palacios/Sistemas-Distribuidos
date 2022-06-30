using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using MySql.Data.MySqlClient;

namespace Company.Function
{
    public static class agregar_carrito
    {
        [FunctionName("agregar_carrito")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Function, "post", Route = null)] HttpRequest req,
            ILogger log)
        {
            String DB_SERVER = Environment.GetEnvironmentVariable("DB_SERVER");
            String DB_USER= Environment.GetEnvironmentVariable("DB_USER");
            String DB_PASS = Environment.GetEnvironmentVariable("DB_PASS");
            String DB_NAME = Environment.GetEnvironmentVariable("DB_NAME");
            String connectionString = $"Server={DB_SERVER}; Port=3306; Database={DB_NAME}; Uid={DB_USER}; Pwd={DB_PASS}; SslMode=Preferred;";

            try{
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                dynamic data = JsonConvert.DeserializeObject(requestBody);

                if (data.descripcion == null || data.descripcion == "") 
                    return new BadRequestObjectResult("Se debe ingresar la descripción");
                if (data.cantidad == null || data.cantidad == 0) 
                    return new BadRequestObjectResult("Se debe ingresar la cantidad");

                var conexion = new MySqlConnection(connectionString);
                conexion.Open();
                MySqlTransaction transaccion = conexion.BeginTransaction();

                try{
                    var cmd_select = new MySqlCommand("SELECT id_articulo, precio, cantidad_almacen FROM articulos WHERE descripcion=@descripcion", conexion);
                    cmd_select.Parameters.AddWithValue("@descripcion", data.descripcion);

                    MySqlDataReader r = cmd_select.ExecuteReader();
                    if(r.Read()){
                        int cantidad = data.cantidad;
                        int id_articulo=r.GetInt32(0);
                        int precio=r.GetInt32(1);
                        int cantidad_almacen=r.GetInt32(2);
                        r.Close();

                        if (cantidad>0 && cantidad<=cantidad_almacen)
                        {
                            var cmd_insert = new MySqlCommand("INSERT INTO carrito_compra VALUES (@id,@cantidad)", conexion);
                            cmd_insert.Parameters.AddWithValue("@id", id_articulo);
                            cmd_insert.Parameters.AddWithValue("@cantidad", cantidad);
                            cmd_insert.ExecuteNonQuery();

                            var cmd_update = new MySqlCommand("UPDATE articulos SET cantidad_almacen=@nueva_cantidad WHERE id_articulo=@id", conexion);
                            cmd_update.Parameters.AddWithValue("@nueva_cantidad", cantidad_almacen - cantidad);
                            cmd_update.Parameters.AddWithValue("@id", id_articulo);
                            cmd_update.ExecuteNonQuery();
                            transaccion.Commit();
                            return new OkObjectResult("Articulo agregado al carrito");
                        }
                        return new BadRequestObjectResult("No hay suficientes existencias del articulo"); 
                    }
                    return new BadRequestObjectResult("El articulo no existe"); 
                }
                catch (System.Exception e){
                    transaccion.Rollback();
                    return new BadRequestObjectResult(e); 
                }
                finally{
                    conexion.Close();    
                }
            }
            catch (System.Exception e){
                return new BadRequestObjectResult(e.Message); 
            }
        }
    }
}

/*
            //transaccion
            conexion.setAutoCommit(false);
            try {
              
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
*/