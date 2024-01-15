package org.example.presentador;


import org.example.modelo.Categoria;
import org.example.modelo.Prestamo;

import java.util.List;

public interface VistaPrestamo {
    void lanzar();
    void setPresentador(PresentadorPrestamo presentador) throws Exception;
    Prestamo getPrestamo();
    void setCategorias(List<Categoria> categorias);

}
