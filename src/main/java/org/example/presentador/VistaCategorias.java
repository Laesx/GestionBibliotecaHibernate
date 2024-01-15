package org.example.presentador;

import org.example.modelo.Categoria;

import java.util.List;

public interface VistaCategorias extends VistaCategoria{
    void setCategorias(List<Categoria> listaCategorias);
}
