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
    public static class consulta_carrito
    {
        [FunctionName("consulta_carrito")]
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

                var conexion = new MySqlConnection(connectionString);
                conexion.Open();

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
                        articulo.cantidad = r.GetInt32(4);
	                    articulo.id_articulo = r.GetInt32(5);
                        articulos.Add(articulo);
                    }
                    r.Close();
                    return new ContentResult{ Content = JsonConvert.SerializeObject(articulos), ContentType="application/json"};
                }
                catch (System.Exception e){
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
