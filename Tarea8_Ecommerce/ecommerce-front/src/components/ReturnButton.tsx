import { Icon } from '@iconify/react'
import React, { ButtonHTMLAttributes } from 'react'
import { useNavigate } from 'react-router-dom';

interface ReturnButtonProps extends ButtonHTMLAttributes<HTMLButtonElement>{
}

export default function ReturnButton({ ...defaultProps}:ReturnButtonProps) {
	const navigate = useNavigate();
	return (
		<button className="flex flex-row justify-start items-center space-x-3 text-gray" onClick={() => navigate(-1)} {...defaultProps} >
			<Icon icon="bx:arrow-back" />
			Volver
		</button>
	)
}
