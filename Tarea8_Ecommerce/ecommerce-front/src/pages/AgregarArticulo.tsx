import { useState } from "react";
import api from "../api";
import Button from "../components/Button";
import Input from "../components/Input";
import InputFile from "../components/InputFile";
import PageLayout from "../components/PageLayout";
import ReturnButton from "../components/ReturnButton";
import TextArea from "../components/TextArea";

export default function AgregarArticulo() {
  const [descripcion, setDescripcion] = useState("");
  const [precio, setPrecio] = useState(1);
  const [cantidad_almacen, setCantidad_almacen] = useState(1);
  const [foto, setFoto] = useState("");
  const [inputKey, setInputKey] = useState("");

  const agregarArticulo =()=>{

    const articulo={
      descripcion,
      precio,
      cantidad_almacen,
      foto
    }
    api.post("alta_articulo", articulo, (code, result)=>{
      if (code == 200){
        setDescripcion("");
        setPrecio(1);
        setCantidad_almacen(1);
        setFoto("");
        setInputKey(Math.random().toString(36))
        alert("articulo agregado");
      }else
        alert(JSON.stringify(result)); 
    });
  }
  
  return (
	  <PageLayout>
      <h1 className="font-black text-2xl">Capturar Articulo</h1>
      <div>
        <span>Descripción</span>
        <TextArea placeholder="Descripcion" value={descripcion} onChange={(e)=>setDescripcion(e.target.value)} />
      </div>
      <div className="flex flex-col">
        <span>Precio</span>
        <Input value={precio} onChange={setPrecio} type="number" min={1} />
      </div>
      <div className="flex flex-col">
        <span>Cantidad en almacén</span>
        <Input value={cantidad_almacen} onChange={setCantidad_almacen} type="number" min={1} />
      </div>
      <div className="flex flex-col">
        <span>Fotografía</span>
        <InputFile onFileSelect={(foto)=>setFoto(foto)} key={inputKey} />
      </div>
      <Button label="Agregar" onClick={agregarArticulo}/>
    </PageLayout>
  )
}
