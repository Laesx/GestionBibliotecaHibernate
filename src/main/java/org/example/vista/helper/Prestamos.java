package org.example.vista.helper;


import org.example.modelo.Prestamo;
import org.example.modelo.dao.*;
import org.example.presentador.PresentadorPrestamo;
import org.example.vista.FichaPrestamo;
import org.example.vista.FormMain;
import org.example.vista.ListaPrestamos;

public class Prestamos {
    public static ListaPrestamos listaPrestamos() throws Exception {
        PrestamoDAO prestamoDAO=new PrestamoDAOImpl();
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        ListaPrestamos listaPrestamos=new ListaPrestamos();
        PresentadorPrestamo presentadorPrestamo=new PresentadorPrestamo(prestamoDAO,categoriaDAO,listaPrestamos);
        listaPrestamos.setPresentador(presentadorPrestamo);
        // PARTE DEL OBSERVER
        presentadorPrestamo.register(FormMain.getInstance());
        listaPrestamos.lanzar();
        return listaPrestamos;
    }

    public static FichaPrestamo fichaPrestamo(Prestamo prestamo) throws Exception {
        PrestamoDAO prestamoDAO=new PrestamoDAOImpl();
        CategoriaDAO categoriaDAO=new CategoriaDAOImpl();
        UsuarioDAO usuarioDAO= new UsuarioDAOImpl();
        LibroDAO libroDAO=new LibroDAOImpl();
        FichaPrestamo fichaPrestamo=new FichaPrestamo(prestamo);
        PresentadorPrestamo presentadorPrestamo=new PresentadorPrestamo(prestamoDAO,categoriaDAO,fichaPrestamo);
        fichaPrestamo.setPresentador(presentadorPrestamo);
        // PARTE DEL OBSERVER
        presentadorPrestamo.register(FormMain.getInstance());
        fichaPrestamo.lanzar();
        return fichaPrestamo;

    }
}
