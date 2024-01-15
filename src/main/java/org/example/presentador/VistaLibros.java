package org.example.presentador;

import org.example.modelo.Libro;

import java.util.List;

public interface VistaLibros extends VistaLibro{
    void setLibros(List<Libro> listaLibros);
}
