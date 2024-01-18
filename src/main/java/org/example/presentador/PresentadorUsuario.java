package org.example.presentador;

import org.example.modelo.dao.UsuarioDAO;
import org.example.vista.observer.Observer;
import org.example.vista.observer.Subject;


public class PresentadorUsuario implements Subject {
    private UsuarioDAO usuarioDAO;
    private VistaUsuario vistaUsuario;

    public PresentadorUsuario(UsuarioDAO usuarioDAO, VistaUsuario vistaUsuario) {
        this.usuarioDAO = usuarioDAO;
        this.vistaUsuario = vistaUsuario;
    }

    public void borra() throws Exception {
        usuarioDAO.borrar(vistaUsuario.getUsuario().getId());
    }

    public void inserta() throws Exception {
        usuarioDAO.insertar(vistaUsuario.getUsuario());
    }

    public void modifica() throws Exception {
        usuarioDAO.modificar(vistaUsuario.getUsuario());
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
    private final Object MUTEX= new Object();
    private boolean changed;

    @Override
    public void register(Observer obj) {
        if (obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX){
            observer=obj;
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (MUTEX){
            observer=null;
        }
    }

    @Override
    public void notifyObservers() throws Exception {
        Observer observerLocal=null;
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized (MUTEX){
            if (!changed)
                return;
            observerLocal=observer;
            this.changed=false;
        }
        if (observerLocal!=null){
            observerLocal.update();
        }

    }

    @Override
    public Object getUpdate(Observer obj) {
        return null;
    }
}
