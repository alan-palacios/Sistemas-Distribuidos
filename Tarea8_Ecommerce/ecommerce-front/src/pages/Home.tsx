import { Icon } from '@iconify/react'
import React from 'react'
import { Link, NavLink } from 'react-router-dom'
import Button from '../components/Button'

export default function Home() {
  return (
	<div className='flex flex-col space-y-10'>
		<NavLink to="/agregar-articulo">
			<button className='flex flex-col bg-blue hover:bg-blue_dark transition hover:ease-in text-white w-40 h-40 rounded-lg text-2xl justify-center items-center'>
				<Icon icon="jam:write-f" width={60}/>
				<span className='font-bold'>Capturar Articulos</span>
			</button>
		</NavLink>
		<NavLink to="/comprar">
			<button className='flex flex-col bg-blue hover:bg-blue_dark transition hover:ease-in text-white w-40 h-40 rounded-lg text-2xl justify-center items-center'>
				<Icon icon="bxs:cart" width={60}/>
				<span className='font-bold'>Comprar Articulos</span>
			</button>
		</NavLink>
	</div>
  )
}
