package org.example.observer;

/**
 * Interfaz que implementa el patrón Observer
 * Esta clase es el Observador, la que "Observa" que se haya producido un cambio
 * Y que se encarga de actualizar su estado
 */
public interface Observer {
    /** Método que se ejecuta cuando un objeto observado cambia
     * @param sub el objeto que ha cambiado
     * @throws Exception en el caso de que no pueda actualizarse el objeto
     */
    void update(Subject sub) throws Exception;
}
