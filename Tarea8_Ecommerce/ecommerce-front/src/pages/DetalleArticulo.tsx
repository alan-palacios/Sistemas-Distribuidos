import { Icon } from '@iconify/react';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import api from '../api';
import Button from '../components/Button';
import Input from '../components/Input';
import PageLayout from '../components/PageLayout'
import { Articulo } from '../types';

interface DetalleArticuloProps{
}
export default function DetalleArticulo({}:DetalleArticuloProps) {
  const params = useParams();
  const [cantidadCarrito, setCantidadCarrito] = useState(1);
  const [articulo, setArticulo] = useState<Articulo>()

  const consultaArticulo =()=>{
    api.post("consulta_articulo", {id_articulo: params.id}, (code, result)=>{
      if (code == 200){
        setArticulo(result)
        if(result.cantidad_almacen<1) setCantidadCarrito(0);
      }else
        alert(JSON.stringify(result)); 
    });
  }
  
  const changeCantidadCarrito = (newCantidad:number)=>{
    if(articulo && articulo.cantidad_almacen){
      if(newCantidad<1) newCantidad=1;
      if(newCantidad>articulo.cantidad_almacen) newCantidad=articulo.cantidad_almacen;
      if(articulo.cantidad_almacen<1) newCantidad=0;
      setCantidadCarrito(newCantidad);
    }
  }

  const agregarCarrito =()=>{
    if(articulo)
      api.post("agregar_carrito", {descripcion: articulo.descripcion, cantidad: cantidadCarrito}, (code, result)=>{
        if (code == 200){
          const articuloNew = {...articulo}
          articuloNew.cantidad_almacen-=cantidadCarrito;
          setArticulo(articuloNew);
          changeCantidadCarrito(articuloNew.cantidad_almacen);
          if(cantidadCarrito>articuloNew.cantidad_almacen) setCantidadCarrito(articuloNew.cantidad_almacen);
          if(articuloNew.cantidad_almacen<1) setCantidadCarrito(0);
          alert('producto agregado al carrito')
        }else
          alert(JSON.stringify(result)); 
      });
  }
  useEffect(() => {
    consultaArticulo();
  }, [])

  return (
    <PageLayout>
      {articulo?
			<div className='bg-white shadow-md flex flex-col justify-center items-center w-full rounded-lg '>
        <div className='w-full flex flex-col space-y-5 '>
				  <img src={`data:image/jpeg;base64,${articulo.foto}`} className="object-cover w-full h-72 rounded-lg" />	
        </div>
        <div className='flex flex-col p-4 space-y-3 w-full'>
          <div className='flex flex-row justify-between items-end w-full text-lg'>
            <span className='text-3xl'>{articulo.descripcion}</span> 
            <span className='font-bold text-4xl'>${55}</span> 
          </div>
          <span className='text-left w-full'>{articulo.cantidad_almacen} disponibles</span>
          <div className='border border-gray w-full my-3' />
          <div className="flex flex-col">
            <span>Cantidad:</span>
            <div className='flex flex-row space-x-2 mt-2'>
              <button className={`text-blue hover:text-blue_dark flex justify-center items-center ${cantidadCarrito<=1?'grayscale':''}`} 
              onClick={()=>changeCantidadCarrito(cantidadCarrito-1)} disabled={cantidadCarrito==1}>
                <Icon icon="akar-icons:circle-minus-fill" width={30}  />
              </button>
              <Input value={cantidadCarrito} onChange={changeCantidadCarrito} type="number" min={1} max={articulo.cantidad_almacen} className="w-20" />
              <button className={`text-blue hover:text-blue_dark flex justify-center items-center ${cantidadCarrito==articulo.cantidad_almacen?'grayscale':''}`} 
              onClick={()=>changeCantidadCarrito(cantidadCarrito+1)} disabled={cantidadCarrito==articulo.cantidad_almacen}>
                <Icon icon="akar-icons:circle-plus-fill" width={30} />
              </button>
            </div>
            <span>Total: {}</span>
          </div>
          <Button label='Agregar al Carrito' onClick={agregarCarrito} />
        </div>
			</div>
      :
        <span>Cargando</span>
      }
    </PageLayout>
  )
}
