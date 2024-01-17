package org.example.presentador;


import org.example.modeloJPA.Categoria;

public interface VistaCategoria {
    void lanzar();
    void setPresentador(PresentadorCategoria presentador) throws Exception;
    Categoria getCategoria();



}
