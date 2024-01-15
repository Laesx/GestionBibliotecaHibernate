package org.example.presentador;
import org.example.modelo.Usuario;

public interface VistaUsuario {
    void lanzar();
    void setPresentador(PresentadorUsuario presentador) throws Exception;
    Usuario getUsuario();

}
