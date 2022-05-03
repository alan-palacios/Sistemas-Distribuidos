import React, { ReactNode } from 'react'
import ReturnButton from './ReturnButton'

interface PageLayoutProps {
	children: ReactNode
}
export default function PageLayout({children}:PageLayoutProps) {
  return (
	<div className="flex flex-col space-y-5 w-full h-screen overflow-y-auto px-8 mt-20 pb-20">
        <ReturnButton/>
		{children}	
	</div>
  )
}
