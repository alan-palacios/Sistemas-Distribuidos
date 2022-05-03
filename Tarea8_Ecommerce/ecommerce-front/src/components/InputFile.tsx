import { ComponentProps, InputHTMLAttributes, useRef, useState } from "react";
import Button from "./Button";

interface InputProps extends InputHTMLAttributes<HTMLInputElement>{
	/**callback for on change event, usualy a setState function */
	onFileSelect?: (value:any )=>void
}

export default function InputFile({value, onFileSelect=(e:string )=>{}, className, ...defaultProps}:InputProps) {
	const [imageSrc, setImageSrc] = useState<string | ArrayBuffer | null>("");
	const fileInput = useRef(null)

    const handleFileInput = (e: any) => {
		const file =e.target.files[0];
		if (!file) return;
		var reader = new FileReader();
		reader.onload = function(e)
		{
		  setImageSrc(reader.result);
		  // reader.result incluye al principio: "data:image/jpeg;base64,"
		  if(reader && reader.result )
        	onFileSelect(reader.result.toString().split(',')[1]);
		};
		reader.readAsDataURL(file);
    }
	return (
		<div className={`bg-white border-2 border-dashed border-gray px-2 py-5 w-full rounded-lg placeholder:text-gray 
			flex flex-col justify-center items-center overflow-hidden relative`} >
			{imageSrc?
				<img src={imageSrc as string}></img>
			:
				<>
					<span>Arrastra el archivo aquí</span>
					<br/>
					<span>ó</span>
				</>
			}
			<Button label="Selecciona un Archivo" />
			<input type='file' id="inputFile" className="absolute inset-0 opacity-0 cursor-pointer w-full h-full"
				onChange={handleFileInput} {...defaultProps} />
		</div>
	)
}