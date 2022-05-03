import { NavLink } from "react-router-dom"

interface ArticuloProps{
	descripcion: string
	precio: number
	cantidad: number
	fotografia: string
}

export default function Articulo({descripcion, precio, cantidad, fotografia}:ArticuloProps) {
  return (
        <div className='flex justify-center items-center'>
			<NavLink to={"/articulo"} className='bg-white shadow-md flex flex-col justify-center items-center w-36 rounded-lg overflow-hidden 
			hover:shadow-blue hover:scale-105 transition-transform ease-in'>
				<img src={fotografia} className="object-cover w-full h-40" />	
				<div className="flex flex-col justify-start w-full px-2 py-1">
					<span className="text-sm text-left">{descripcion}</span>
					<div className="flex flex-row justify-between items-center">
						<span className="text-xl font-semibold">${precio}</span>
						<span className="text-xs">{`${cantidad} disponible(s)`}</span>
					</div>
				</div>
			</NavLink>
        </div>
  )
}
