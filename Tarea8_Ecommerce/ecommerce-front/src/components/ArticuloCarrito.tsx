import { Icon } from "@iconify/react"
import api from "../api"

interface ArticuloProps{
	id_articulo: number
	descripcion: string
	precio: number
	cantidadComprada: number
	fotografia: string
}

export default function ArticuloCarrito({id_articulo, descripcion, precio, cantidadComprada, fotografia}:ArticuloProps) {
  const eliminarDelCarrito =()=>{
    api.post("borra_articulo_carrito", {id_articulo}, (code, result)=>{
      if (code == 200){
        console.log(result);
		alert("articulo eliminado");
		location.reload();
      }else
        alert(JSON.stringify(result)); 
    });
  }
  return (
		<div className='bg-white flex flex-row justify-center items-center w-full '>
			<div className=" rounded-lg overflow-hidden relative">
				<img src={fotografia} className="object-cover h-14 w-44" />	
				<button className='bg-blue hover:bg-blue_dark text-white rounded-bl-lg rounded-tr-lg 
				flex justify-center items-center w-7 h-7 absolute bottom-0 left-0' onClick={eliminarDelCarrito}>
				  	<Icon icon="bxs:trash" width={20} />
				</button>
			</div>
			<div className="w-full pr-2 pl-5">
				<span className=" text-left">{descripcion}</span>
				<div className="flex flex-row justify-between items-center text-xl font-semibold">
					<span >${precio} X {cantidadComprada}</span>
					<span >${precio*cantidadComprada}</span>
				</div>
			</div>
		</div>
  )
}
