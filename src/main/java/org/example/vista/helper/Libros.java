package org.example.vista.helper;


import org.example.modelo.BusquedaLibro;
import org.example.modelo.Libro;
import org.example.modelo.dao.CategoriaDAO;
import org.example.modelo.dao.CategoriaDAOImpl;
import org.example.modelo.dao.LibroDAOImpl;
import org.example.presentador.PresentadorLibro;
import org.example.vista.FichaLibro;
import org.example.vista.FormMain;
import org.example.vista.ListaLibros;
import org.example.vista.SeleccionaLibro;

import java.awt.*;

public class Libros {
    public static ListaLibros listaLibros() throws Exception {
        LibroDAOImpl libroDAO=new LibroDAOImpl();
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        ListaLibros listaLibros=new ListaLibros();
        PresentadorLibro presentadorLibro=new PresentadorLibro(libroDAO,categoriaDAO,listaLibros);
        // PARTE DEL OBSERVER
        libroDAO.register(FormMain.getInstance());
        listaLibros.setPresentador(presentadorLibro);
        listaLibros.lanzar();
        return listaLibros;
    }

    public static SeleccionaLibro seleccionaLibro(Frame owner, String title, boolean modal, BusquedaLibro busquedaLibro) throws Exception {
        LibroDAOImpl libroDAO=new LibroDAOImpl();
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        SeleccionaLibro seleccionaLibro=new SeleccionaLibro(owner,title,modal,busquedaLibro);
        PresentadorLibro presentadorLibro=new PresentadorLibro(libroDAO,categoriaDAO,seleccionaLibro);
        // PARTE DEL OBSERVER
        libroDAO.register(FormMain.getInstance());
        seleccionaLibro.setPresentador(presentadorLibro);
        seleccionaLibro.lanzar();
        return seleccionaLibro;
    }

    public static FichaLibro fichaLibro(Libro libro) throws Exception {
        LibroDAOImpl libroDAO=new LibroDAOImpl();
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        FichaLibro fichaLibro=new FichaLibro(libro);
        PresentadorLibro presentadorLibro=new PresentadorLibro(libroDAO,categoriaDAO,fichaLibro);
        // PARTE DEL OBSERVER
        libroDAO.register(FormMain.getInstance());
        fichaLibro.setPresentador(presentadorLibro);
        fichaLibro.lanzar();
        return fichaLibro;

    }
}
