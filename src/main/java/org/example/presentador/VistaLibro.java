package org.example.presentador;


import org.example.modelo.Categoria;
import org.example.modelo.Libro;

import java.util.List;

public interface VistaLibro {
    void lanzar();
    void setPresentador(PresentadorLibro presentador) throws Exception;
    Libro getLibro();

    void setCategorias(List<Categoria> categorias);
}
