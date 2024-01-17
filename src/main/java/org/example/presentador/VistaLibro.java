package org.example.presentador;


import org.example.modelo.Categoria;
import org.example.modeloJPA.Libro;

import java.util.List;

public interface VistaLibro {
    void setCategorias(List<org.example.modeloJPA.Categoria> categorias);

    void lanzar();
    void setPresentador(PresentadorLibro presentador) throws Exception;
    Libro getLibro();

    void setCategorias(List<Categoria> categorias);
}
