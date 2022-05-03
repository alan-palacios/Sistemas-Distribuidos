import React, { useEffect, useState } from 'react'
import api from '../api'
import ArticuloCarrito from '../components/ArticuloCarrito'
import Button from '../components/Button'
import PageLayout from '../components/PageLayout'
import { Articulo } from '../types'

export default function Carrito() {
  const [articulos, setArticulos] = useState<Array<Articulo>>();
  const [total, setTotal] = useState(0);
  const consultaCarrito =()=>{
    api.post("consulta_carrito", {}, (code, result)=>{
      if (code == 200){
        console.log(result);
        setArticulos(result); 
        let calculoTotal=0;
        result?.forEach((articulo:Articulo)=>{
          if(articulo.cantidad)
          calculoTotal+=articulo.precio*articulo.cantidad;
        })
        setTotal(calculoTotal);
      }else
        alert(JSON.stringify(result)); 
    });
  }

  useEffect(() => {
    consultaCarrito();
  }, [])
  
  const renderArticulos =()=>{
    if(articulos){
      const articulosRender= articulos.map((articulo,i)=>{
                if(articulo.cantidad){
                  return(
                  <ArticuloCarrito id_articulo={articulo.id_articulo} descripcion={articulo.descripcion} precio={articulo.precio} cantidadComprada={articulo.cantidad} 
                    key={i} fotografia={`data:image/jpeg;base64,${articulo.foto}`} />
                  )
                }
              })
      return articulosRender;
    }
  }

  const eliminarCarrito =()=>{
    api.post("borra_carrito", {}, (code, result)=>{
      if (code == 200){
        console.log(result);
        alert('carrito eliminado')
        location.reload();
      }else
        alert(JSON.stringify(result)); 
    });
  }

  return (
    <PageLayout>
      <h1 className="font-black text-2xl">Mi Carrito</h1>
      {articulos?
        articulos.length==0?
          <span>Aún no tienes productos en tu carrito</span>
        :
        <>
          <div className='bg-white shadow-md flex flex-col p-4 justify-center items-center w-full rounded-lg '>
            <div className='w-full flex flex-col space-y-5'>
              {renderArticulos()}
            </div>
            <div className='border border-gray w-full my-3' />
            <div className='flex flex-row justify-end items-end w-full text-lg px-2'>
              <span className='mr-10'>Total:</span> 
              <span className='font-bold'>${total}</span> 
            </div>
          </div>
          <Button label='Eliminar Carrito' onClick={eliminarCarrito} />
        </>
      :
        <span>Cargando...</span>
      }
    </PageLayout>
  )
}
