package org.example.presentador;

import org.example.modelo.dao.CategoriaDAO;
import org.example.observer.Observer;
import org.example.observer.Subject;


public class PresentadorCategoria implements Subject {
    private CategoriaDAO categoriaDAO;
    private VistaCategoria vistaCategoria;

    public PresentadorCategoria(CategoriaDAO categoriaDAO, VistaCategoria vistaCategoria) {
        this.categoriaDAO = categoriaDAO;
        this.vistaCategoria = vistaCategoria;
    }

    public void borra() throws Exception {
        categoriaDAO.borrar(vistaCategoria.getCategoria().getId());
        notifyObservers();
    }

    public void inserta() throws Exception {
        categoriaDAO.inserta(vistaCategoria.getCategoria());
        notifyObservers();
    }

    public void modifica() throws Exception {
        categoriaDAO.modificar(vistaCategoria.getCategoria());
        notifyObservers();
    }

    public void listaAllCategorias() throws Exception {
        VistaCategorias vistaCategorias = (VistaCategorias) vistaCategoria;
        vistaCategorias.setCategorias(categoriaDAO.leerAllCategorias());
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
