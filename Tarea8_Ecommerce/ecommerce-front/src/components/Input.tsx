import { ComponentProps, InputHTMLAttributes, useState } from "react";

interface InputProps extends InputHTMLAttributes<HTMLInputElement>{
	/**callback for on change event, usualy a setState function */
	onChange?: (value:any )=>void
}

export default function Input({value, onChange=(e:string )=>{}, ...defaultProps}:InputProps) {
	return (
		<input value={value} onChange={(e)=>onChange(e.target.value)} {...defaultProps}
			className={`bg-white border-2 border-gray px-2 py-1 w-full rounded-lg
			placeholder:text-gray  ${defaultProps.className} `} >
		</input>
	)
}