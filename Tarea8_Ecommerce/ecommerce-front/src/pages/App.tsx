import { BrowserRouter, Route, Routes } from "react-router-dom";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import AgregarArticulo from "./AgregarArticulo";
import Carrito from "./Carrito";
import ComprarArticulo from "./ComprarArticulo";
import DetalleArticulo from "./DetalleArticulo";
import Home from "./Home";

function App() {
  return (
    <div className="flex flex-col items-center justify-center h-full bg-background relative">
      <BrowserRouter>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home/>} />
          <Route path="/agregar-articulo" element={<AgregarArticulo/>} />
          <Route path="/:id" element={<DetalleArticulo/>} />
          <Route path="/comprar" element={<ComprarArticulo/>} />
          <Route path="/carrito" element={<Carrito/>} />
        </Routes>
      </BrowserRouter>
      <Footer /> 
    </div>
  );
}

export default App;
