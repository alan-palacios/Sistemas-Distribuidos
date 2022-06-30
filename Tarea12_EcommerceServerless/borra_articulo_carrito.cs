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
    public static class borra_articulo_carrito
    {
        [FunctionName("borra_articulo_carrito")]
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

                if (data.id_articulo == null ) 
                    return new BadRequestObjectResult("Se debe ingresar el id del articulo");

                var conexion = new MySqlConnection(connectionString);
                conexion.Open();
                MySqlTransaction transaccion = conexion.BeginTransaction();

                try{
                    var cmd_select = new MySqlCommand("SELECT id_articulo, cantidad FROM carrito_compra WHERE id_articulo=@id", conexion);
                    cmd_select.Parameters.AddWithValue("@id", data.id_articulo);
                    MySqlDataReader r = cmd_select.ExecuteReader();

                    if(r.Read()){
                        int cantidad = r.GetInt32(1);
                        r.Close();

                        var cmd_delete = new MySqlCommand("DELETE FROM carrito_compra where id_articulo=@id", conexion);
                        cmd_delete.Parameters.AddWithValue("@id", data.id_articulo);
                        cmd_delete.ExecuteNonQuery();

                        var cmd_update = new MySqlCommand("UPDATE articulos SET `cantidad_almacen`=`cantidad_almacen` + @cantidad WHERE id_articulo=@id", conexion);
                        cmd_update.Parameters.AddWithValue("@cantidad", cantidad);
                        cmd_update.Parameters.AddWithValue("@id", data.id_articulo);
                        cmd_update.ExecuteNonQuery();
                        transaccion.Commit();
                        return new OkObjectResult("Articulo eliminado del carrito");
                    }
                    return new BadRequestObjectResult("El articulo no existe"); 
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
