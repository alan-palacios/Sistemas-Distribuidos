import React, { TextareaHTMLAttributes } from 'react'

interface TextAreaProps extends TextareaHTMLAttributes<HTMLTextAreaElement>{
}
export default function TextArea({...defaultProps}:TextAreaProps) {
	return (
		<textarea className={`bg-white border-2 border-gray px-2 py-1 w-full h-40 rounded-lg
			placeholder:text-gray  ${defaultProps.className}`} 
			{...defaultProps}>
		</textarea>
	)
}
