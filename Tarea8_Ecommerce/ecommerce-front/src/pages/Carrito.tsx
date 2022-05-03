import React from 'react'
import ArticuloCarrito from '../components/ArticuloCarrito'
import Button from '../components/Button'
import PageLayout from '../components/PageLayout'

export default function Carrito() {
  return (
    <PageLayout>
      <h1 className="font-black text-2xl">Mi Carrito</h1>
			<div className='bg-white shadow-md flex flex-col p-4 justify-center items-center w-full rounded-lg '>
        <div className='w-full flex flex-col space-y-5'>
          <ArticuloCarrito descripcion='E Pura 1lt.' precio={10} cantidadComprada={1} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <ArticuloCarrito descripcion='E Pura 1lt.' precio={10} cantidadComprada={3} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <ArticuloCarrito descripcion='E Pura 1lt.' precio={10} cantidadComprada={5} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
          <ArticuloCarrito descripcion='E Pura 1lt.' precio={10} cantidadComprada={2} fotografia="https://epura.com.mx/development/wp-content/uploads/2017/04/001-1.jpg" />
        </div>
        <div className='border border-gray w-full my-3' />
        <div className='flex flex-row justify-end items-end w-full text-lg px-2'>
          <span className='mr-10'>Total:</span> 
          <span className='font-bold'>${55}</span> 
        </div>
			</div>
      <Button label='Eliminar Carrito' />
    </PageLayout>
  )
}
