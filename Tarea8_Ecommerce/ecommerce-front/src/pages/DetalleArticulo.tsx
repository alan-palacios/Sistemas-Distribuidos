import { Icon } from '@iconify/react';
import React, { useState } from 'react'
import { useParams } from 'react-router-dom';
import Button from '../components/Button';
import Input from '../components/Input';
import PageLayout from '../components/PageLayout'

interface DetalleArticuloProps{
}
export default function DetalleArticulo({}:DetalleArticuloProps) {
  const params = useParams();
  const [cantidadCarrito, setCantidadCarrito] = useState(1);
  const [articulo, setArticulo] = useState({
    descripcion: "E Pura",
    precio: 10,
    cantidadDisponible: 5,
    fotografia: "https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg"
  })
  
  const changeCantidadCarrito = (newCantidad:number)=>{
    if(newCantidad<1) newCantidad=1;
    if(newCantidad>articulo.cantidadDisponible) newCantidad=articulo.cantidadDisponible;
    setCantidadCarrito(newCantidad);
  }

  return (
    <PageLayout>
			<div className='bg-white shadow-md flex flex-col justify-center items-center w-full rounded-lg '>
        <div className='w-full flex flex-col space-y-5 '>
				  <img src={articulo.fotografia} className="object-cover w-full h-72 rounded-lg" />	
        </div>
        <div className='flex flex-col p-4 space-y-3 w-full'>
          <div className='flex flex-row justify-between items-end w-full text-lg'>
            <span className='text-3xl'>{articulo.descripcion}</span> 
            <span className='font-bold text-4xl'>${55}</span> 
          </div>
          <span className='text-left w-full'>{articulo.cantidadDisponible} disponibles</span>
          <div className='border border-gray w-full my-3' />
          <div className="flex flex-col">
            <span>Cantidad:</span>
            <div className='flex flex-row space-x-2 mt-2'>
              <button className={`text-blue hover:text-blue_dark flex justify-center items-center ${cantidadCarrito==1?'grayscale':''}`} 
              onClick={()=>changeCantidadCarrito(cantidadCarrito-1)} disabled={cantidadCarrito==1}>
                <Icon icon="akar-icons:circle-minus-fill" width={30}  />
              </button>
              <Input value={cantidadCarrito} onChange={changeCantidadCarrito} type="number" min={1} max={articulo.cantidadDisponible} className="w-20" />
              <button className={`text-blue hover:text-blue_dark flex justify-center items-center ${cantidadCarrito==articulo.cantidadDisponible?'grayscale':''}`} 
              onClick={()=>changeCantidadCarrito(cantidadCarrito+1)} disabled={cantidadCarrito==articulo.cantidadDisponible}>
                <Icon icon="akar-icons:circle-plus-fill" width={30} />
              </button>
            </div>
            <span>Total: {}</span>
          </div>
          <Button label='Agregar al Carrito' />
        </div>
			</div>
    </PageLayout>
  )
}
