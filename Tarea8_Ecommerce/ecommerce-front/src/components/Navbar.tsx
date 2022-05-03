import { Icon } from "@iconify/react";
import { NavLink } from "react-router-dom";

export default function Navbar() {
	return (
        <nav className="w-full h-14 bg-blue flex justify-between fixed top-0 px-3">
            <NavLink to={"/"} className=" w-36 flex justify-center text-center items-center ">
                <span className='text-xl text-white font-bold '>E-Commerce</span>
            </NavLink>
            <NavLink to={"/carrito"} className="flex justify-center text-center items-center text-white">
                <Icon icon="akar-icons:cart" width={30} /> 
            </NavLink>
        </nav>
	)
}
