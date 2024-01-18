package org.example.vista.helper;


import org.example.modelo.BusquedaUsuario;
import org.example.modelo.Usuario;
import org.example.modelo.dao.UsuarioDAO;
import org.example.modelo.dao.UsuarioDAOImpl;
import org.example.presentador.PresentadorUsuario;
import org.example.vista.FichaUsuario;
import org.example.vista.FormMain;
import org.example.vista.ListaUsuarios;
import org.example.vista.SeleccionaUsuario;

import java.awt.*;

public class Usuarios {
    public static ListaUsuarios listaUsuarios() throws Exception {
        UsuarioDAO usuarioDAO=new UsuarioDAOImpl();
        ListaUsuarios listaUsuarios=new ListaUsuarios();
        PresentadorUsuario presentadorUsuario=new PresentadorUsuario(usuarioDAO,listaUsuarios);
        // PARTE DEL OBSERVER
        presentadorUsuario.register(FormMain.getInstance());
        listaUsuarios.setPresentador(presentadorUsuario);
        listaUsuarios.lanzar();
        return listaUsuarios;
    }

    public static SeleccionaUsuario seleccionaUsuario(Frame owner, String title, boolean modal, BusquedaUsuario busquedaUsuario) throws Exception {
        UsuarioDAO usuarioDAO=new UsuarioDAOImpl();
        SeleccionaUsuario seleccionaUsuario=new SeleccionaUsuario(owner, title, modal,busquedaUsuario);
        PresentadorUsuario presentadorUsuario=new PresentadorUsuario(usuarioDAO,seleccionaUsuario);
        seleccionaUsuario.setPresentador(presentadorUsuario);
        // PARTE DEL OBSERVER
        presentadorUsuario.register(FormMain.getInstance());
        seleccionaUsuario.lanzar();
        return seleccionaUsuario;
    }

    public static FichaUsuario fichaUsuario(Usuario usuario) throws Exception {
        UsuarioDAO usuarioDAO=new UsuarioDAOImpl();
        FichaUsuario fichaUsuario=new FichaUsuario(usuario);
        PresentadorUsuario presentadorUsuario=new PresentadorUsuario(usuarioDAO,fichaUsuario);
        fichaUsuario.setPresentador(presentadorUsuario);
        // PARTE DEL OBSERVER
        presentadorUsuario.register(FormMain.getInstance());
        fichaUsuario.lanzar();
        return fichaUsuario;

    }
}
