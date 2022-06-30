import { Icon } from '@iconify/react';
import React, { useEffect, useState } from 'react'
import api from '../api';
import ArticuloItem from '../components/ArticuloItem';
import Input from '../components/Input'
import PageLayout from '../components/PageLayout'
import ReturnButton from '../components/ReturnButton'
import { Articulo } from '../types';

export default function ComprarArticulo() {
  const [busqueda, setBusqueda] = useState(""); 
  const [articulos, setArticulos] = useState<Array <Articulo>>();

  const buscarArticulos =()=>{
    api.post("buscar_articulos", {descripcion: busqueda}, (code, result)=>{
      if (code == 200){
        console.log(result);
        setArticulos(result);
      }else
        alert(JSON.stringify(result)); 
    });
  }
  useEffect(() => {
    buscarArticulos();
  }, [])
  
  return (
	  <PageLayout>
      <h1 className="font-black text-2xl">Comprar Articulos</h1>
      <div>
        <span>Buscar</span>
        <div className='flex flex-row space-x-1'>
          <Input value={busqueda} onChange={setBusqueda} placeholder="Agua..." /> 
          <button className='bg-blue hover:bg-blue_dark text-white rounded-lg flex justify-center items-center w-20'
          onClick={buscarArticulos}>
            <Icon icon="fa:search" width={20} height={20} />
          </button>
        </div>
      </div>
      <div className=' pb-32 pt-2 px-2 scroll-auto overflow-y-auto '>
        <div className='grid gap-3 grid-cols-2'>
          {articulos && articulos?.length<1 &&
            <span>No hay articulos que coincidan con la busqueda</span>
          }
          {articulos && articulos.length>0 && articulos.map((articulo)=>(
            <ArticuloItem descripcion={articulo.descripcion} precio={articulo.precio} cantidad={articulo.cantidad_almacen} 
              key={articulo.id_articulo} fotografia={`data:image/jpeg;base64,${articulo.foto}`} id_articulo={articulo.id_articulo} />
          ))}
        </div>
      </div>
    </PageLayout>
  )
}
