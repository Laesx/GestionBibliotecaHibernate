package org.example.presentador;

import org.example.modelo.dao.UsuarioDAO;
import org.example.observer.Observer;
import org.example.observer.Subject;
import org.example.vista.FichaPrestamo;
import org.example.vista.FormMain;


public class PresentadorUsuario implements Subject {
    private UsuarioDAO usuarioDAO;
    private VistaUsuario vistaUsuario;

    private FormMain formMain;

    public PresentadorUsuario(UsuarioDAO usuarioDAO, VistaUsuario vistaUsuario) {
        this.usuarioDAO = usuarioDAO;
        this.vistaUsuario = vistaUsuario;
        formMain=FormMain.getInstance();
        register(formMain);
    }

    public void borra() throws Exception {
        usuarioDAO.borrar(vistaUsuario.getUsuario().getId());
        notifyObservers();
    }

    public void inserta() throws Exception {
        usuarioDAO.insertar(vistaUsuario.getUsuario());
        notifyObservers();
    }

    public void modifica() throws Exception {
        usuarioDAO.modificar(vistaUsuario.getUsuario());
        notifyObservers();
    }

    public void listaAllUsuarios() throws Exception {
        VistaUsuarios vistaUsuarios = (VistaUsuarios) vistaUsuario;
        vistaUsuarios.setUsuarios(usuarioDAO.leerAllUsuarios());
    }

    public void leerUsuariosOR(int id,String nombre,String apellidos) throws Exception {
        VistaUsuarios vistaUsuarios = (VistaUsuarios) vistaUsuario;
        vistaUsuarios.setUsuarios(usuarioDAO.leerUsuariosOR(id,nombre,apellidos));
    }

    // Se puede implementar con una lista para poder tener varios Observer
    // Pero en el uso que le vamos a dar solo necesitamos un Observer
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
