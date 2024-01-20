package org.example.presentador;

import org.example.modelo.dao.CategoriaDAO;
import org.example.modelo.dao.LibroDAO;
import org.example.observer.Observer;
import org.example.observer.Subject;

public class PresentadorLibro implements Subject {
    private LibroDAO libroDAO;
    private CategoriaDAO categoriaDAO;
    private VistaLibro vistaLibro;

    public PresentadorLibro(LibroDAO libroDAO, CategoriaDAO categoriaDAO,VistaLibro vistaLibro) {
        this.libroDAO = libroDAO;
        this.categoriaDAO = categoriaDAO;
        this.vistaLibro = vistaLibro;
    }
    public void borra() throws Exception {
        libroDAO.borrar(vistaLibro.getLibro().getId());
        notifyObservers();
    }

    public void inserta() throws Exception {
        libroDAO.insertar(vistaLibro.getLibro());
        notifyObservers();
    }

    public void modifica() throws Exception {
        libroDAO.modificar(vistaLibro.getLibro());
        notifyObservers();
    }

    public void listaAllLibros() throws Exception {
        VistaLibros vistaLibros = (VistaLibros) vistaLibro;
        vistaLibros.setLibros(libroDAO.leerAllLibros());
    }
    public void listaAllCategorias(){
        try {
            vistaLibro.setCategorias(categoriaDAO.leerAllCategorias());
        } catch (Exception e){
            vistaLibro.setCategorias(null);
        }
    }
    public void leerLibrosOR(int id, String titulo, String autor, String editorial, int categoria) throws Exception {
        VistaLibros vistaLibros = (VistaLibros) vistaLibro;
        vistaLibros.setLibros(libroDAO.leerLibrosOR(id,titulo,autor,editorial,categoria));
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

    @Override
    public Object getUpdate(Observer obj) {
        return null;
    }
}
