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
using System.Collections;

namespace Company.Function
{
    public static class borra_carrito
    {
        [FunctionName("borra_carrito")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Function, "get", "post", Route = null)] HttpRequest req,
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

                var conexion = new MySqlConnection(connectionString);
                conexion.Open();
                MySqlTransaction transaccion = conexion.BeginTransaction();

                try{
                    var cmd_select = new MySqlCommand("SELECT a.descripcion, a.precio, length(b.foto), b.foto, c.cantidad, a.id_articulo FROM carrito_compra c INNER JOIN articulos a ON c.id_articulo=a.id_articulo INNER JOIN fotos_articulos b ON c.id_articulo=b.id_articulo", conexion);
                    MySqlDataReader r = cmd_select.ExecuteReader();
                    ArrayList articulos = new ArrayList();

                    while(r.Read()){
                        Articulo articulo = new Articulo();
                        articulo.descripcion = r.GetString(0);
                        articulo.precio = r.GetInt32(1);
                        if(!r.IsDBNull(3)){
                            int longitud = r.GetInt32(2);
                            articulo.foto = new Byte[longitud];
                            r.GetBytes(3, 0, articulo.foto, 0, longitud );
                        }
                        articulo.cantidad= r.GetInt32(4);
	                    articulo.id_articulo = r.GetInt32(5);
                        articulos.Add(articulo);
                    }
                    r.Close();

                    var cmd_delete = new MySqlCommand("DELETE FROM carrito_compra", conexion);
                    cmd_delete.ExecuteNonQuery();
                    
                    foreach (Articulo articulo in articulos)
                    {
                        var cmd_update = new MySqlCommand("UPDATE articulos SET `cantidad_almacen`=`cantidad_almacen`+@cantidad WHERE id_articulo=@id", conexion);
                        cmd_update.Parameters.AddWithValue("@cantidad", articulo.cantidad);
                        cmd_update.Parameters.AddWithValue("@id", articulo.id_articulo);
                        cmd_update.ExecuteNonQuery();
                    }
                    transaccion.Commit();

                    return new OkObjectResult("Carrito eliminado");
                }
                catch (System.Exception e){
                    transaccion.Rollback();
                    return new BadRequestObjectResult(e.Message); 
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
