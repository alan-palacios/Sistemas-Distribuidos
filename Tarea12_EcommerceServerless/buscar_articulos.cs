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
    public static class buscar_articulos
    {
        [FunctionName("buscar_articulos")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Function, "post", Route = null)] HttpRequest req,
            ILogger log)
        {
            //log.LogInformation("C# HTTP trigger function processed a request.");
            String DB_SERVER = Environment.GetEnvironmentVariable("DB_SERVER");
            String DB_USER= Environment.GetEnvironmentVariable("DB_USER");
            String DB_PASS = Environment.GetEnvironmentVariable("DB_PASS");
            String DB_NAME = Environment.GetEnvironmentVariable("DB_NAME");
            String connectionString = $"Server={DB_SERVER}; Port=3306; Database={DB_NAME}; Uid={DB_USER}; Pwd={DB_PASS}; SslMode=Preferred;";

            try{
                string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
                dynamic data = JsonConvert.DeserializeObject(requestBody);

                if (data.descripcion == null) 
                    return new BadRequestObjectResult("Se debe ingresar un termino de busqueda");

                var conexion = new MySqlConnection(connectionString);
                conexion.Open();

                try{
                    var cmd_select = new MySqlCommand("SELECT  a.descripcion, a.precio,a.cantidad_almacen, length(b.foto), b.foto, a.id_articulo FROM articulos a LEFT OUTER JOIN fotos_articulos b ON a.id_articulo=b.id_articulo WHERE a.descripcion LIKE CONCAT( '%',@descripcion,'%')", conexion);
                    cmd_select.Parameters.AddWithValue("@descripcion", data.descripcion);
                    MySqlDataReader r = cmd_select.ExecuteReader();
                    ArrayList articulos = new ArrayList();
                    while(r.Read()){
                        Articulo articulo = new Articulo();
                        articulo.descripcion = r.GetString(0);
                        articulo.precio = r.GetInt32(1);
                        articulo.cantidad_almacen = r.GetInt32(2);
                        if(!r.IsDBNull(4)){
                            int longitud = r.GetInt32(3);
                            articulo.foto = new Byte[longitud];
                            r.GetBytes(4, 0, articulo.foto, 0, longitud );
                        }
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
