import React, { ButtonHTMLAttributes } from 'react'

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement>{
	/** Button content */
  	label: string;
  	/** append styling */
  	className?: string
  	/** Button type */
  	selected?: boolean 
  	/** Button type */
  	color?: 'yellow' | 'green' | 'cyan' | 'black' | 'red'
  	/** onClick handler*/
}

export default function Button({label, className, selected=false, color='yellow', ...defaultProps}:ButtonProps) {
	return (
		<button className={`bg-blue hover:bg-blue_dark text-white py-2 px-3 rounded-lg  ${className}`}
		{...defaultProps}>
			{label}	
		</button>
	)
}
