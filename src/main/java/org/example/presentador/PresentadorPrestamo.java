package org.example.presentador;

import org.example.modelo.dao.CategoriaDAO;
import org.example.modelo.dao.PrestamoDAO;
import org.example.observer.Observer;
import org.example.observer.Subject;

public class PresentadorPrestamo implements Subject {
    private PrestamoDAO prestamoDAO;
    private CategoriaDAO categoriaDAO;
    private VistaPrestamo vistaPrestamo;

    public PresentadorPrestamo(PrestamoDAO prestamoDAO, CategoriaDAO categoriaDAO, VistaPrestamo vistaPrestamo) {
        this.prestamoDAO = prestamoDAO;
        this.categoriaDAO = categoriaDAO;
        this.vistaPrestamo = vistaPrestamo;
    }

    public void borra() throws Exception {
        prestamoDAO.borrar(vistaPrestamo.getPrestamo().getId());
        notifyObservers();
    }

    public void inserta() throws Exception {
        prestamoDAO.insertar(vistaPrestamo.getPrestamo());
        notifyObservers();
    }

    public void modifica() throws Exception {
        prestamoDAO.modificar(vistaPrestamo.getPrestamo());
        notifyObservers();
    }

    public void listaAllPrestamos() throws Exception {
        VistaPrestamos vistaPrestamos = (VistaPrestamos) vistaPrestamo;
        vistaPrestamos.setPrestamos(prestamoDAO.leerAllPrestamos());
    }

    public void listaAllCategorias() {
        try {
            vistaPrestamo.setCategorias(categoriaDAO.leerAllCategorias());
        } catch (Exception e){
            vistaPrestamo.setCategorias(null);
        }
    }
    private Observer observer;

    @Override
    public void register(Observer obj){
        if (obj == null) throw new NullPointerException("Null Observer");
        observer=obj;
    }

    @Override
    public void unregister(Observer obj) {
        observer=null;
    }

    @Override
    public void notifyObservers() throws Exception {
        if (observer!=null){
            observer.update(this);
        }
    }
}
