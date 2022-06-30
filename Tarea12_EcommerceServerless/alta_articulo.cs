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
    public static class alta_articulo
    {
        [FunctionName("alta_articulo")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Function, "post", Route = null)] HttpRequest req,
            ILogger log)
        {
            try{
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                dynamic data = JsonConvert.DeserializeObject(requestBody);

                if (data.descripcion == null || data.descripcion == "") 
                    return new BadRequestObjectResult("Se debe ingresar la descripción");

                if (data.precio == null || data.precio<1)
                    return new BadRequestObjectResult("Se debe ingresar el precio");

                if (data.cantidad_almacen == null || data.cantidad_almacen<1)
                    return new BadRequestObjectResult("Se debe ingresar la cantidad disponible en almacen");

                if (data.foto == null || data.foto == "")
                    return new BadRequestObjectResult("Se debe ingresar una foto");
                
                String DB_SERVER = Environment.GetEnvironmentVariable("DB_SERVER");
                Console.WriteLine(DB_SERVER);
                String DB_USER= Environment.GetEnvironmentVariable("DB_USER");
                String DB_PASS = Environment.GetEnvironmentVariable("DB_PASS");
                String DB_NAME = Environment.GetEnvironmentVariable("DB_NAME");
                String connectionString = $"Server={DB_SERVER}; Port=3306; Database={DB_NAME}; Uid={DB_USER}; Pwd={DB_PASS}; SslMode=Preferred;";
                var conexion = new MySqlConnection(connectionString);
                conexion.Open();
                MySqlTransaction transaccion = conexion.BeginTransaction();
                try{
                    var cmd_select = new MySqlCommand();
                    cmd_select.Connection= conexion;
                    cmd_select.CommandText="SELECT id_articulo FROM articulos WHERE descripcion=@descripcion";
                    cmd_select.Parameters.AddWithValue("@descripcion", data.descripcion);
                    MySqlDataReader r = cmd_select.ExecuteReader();
                    if(r.Read()){
                        return new BadRequestObjectResult("El articulo ya existe"); 
                    }
                    r.Close();

                    var cmd_insert = new MySqlCommand();
                    cmd_insert.Connection = conexion;
                    cmd_insert.CommandText ="INSERT INTO articulos VALUES (0, @descripcion, @precio, @cantidad)";
                    cmd_insert.Parameters.AddWithValue("@descripcion", data.descripcion);
                    cmd_insert.Parameters.AddWithValue("@precio", data.precio);
                    cmd_insert.Parameters.AddWithValue("@cantidad", data.cantidad_almacen);
                    cmd_insert.ExecuteNonQuery();

                    if (data.foto != null)
                    {
                        var cmd_insert_foto = new MySqlCommand();
                        cmd_insert_foto.Connection = conexion;
                        cmd_insert_foto.CommandText ="INSERT INTO fotos_articulos VALUES (0, @foto, (SELECT id_articulo FROM articulos WHERE descripcion = @descripcion))";
                        cmd_insert_foto.Parameters.AddWithValue("@foto", Convert.FromBase64String((string)data.foto));
                        cmd_insert_foto.Parameters.AddWithValue("@descripcion", data.descripcion);
                        cmd_insert_foto.ExecuteNonQuery();
                    }
                    transaccion.Commit();
                    return new OkObjectResult("Articulo insertado");
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
