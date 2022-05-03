import { Icon } from '@iconify/react';
import React, { useState } from 'react'
import Articulo from '../components/Articulo';
import Input from '../components/Input'
import PageLayout from '../components/PageLayout'
import ReturnButton from '../components/ReturnButton'

export default function ComprarArticulo() {
  const [busqueda, setBusqueda] = useState(""); 
  return (
	  <PageLayout>
      <h1 className="font-black text-2xl">Comprar Articulos</h1>
      <div>
        <span>Buscar</span>
        <div className='flex flex-row space-x-1'>
          <Input value={busqueda} onChange={setBusqueda} placeholder="Agua..." /> 
          <button className='bg-blue hover:bg-blue_dark text-white rounded-lg flex justify-center items-center w-20'>
            <Icon icon="fa:search" width={20} height={20} />
          </button>
        </div>
      </div>
      <div className=' pb-32 pt-2 px-2 scroll-auto overflow-y-auto '>
        <div className='grid gap-3 grid-cols-2'>
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <Articulo descripcion='E Pura 1lt.' precio={10} cantidad={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
        </div>
      </div>
    </PageLayout>
  )
}
