package org.example.presentador;

import org.example.modelo.Usuario;

import java.util.List;

public interface VistaUsuarios extends VistaUsuario{
    void setUsuarios(List<Usuario> listaUsuarios);
}
