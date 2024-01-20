package org.example.observer;

/**
 * Interfaz que implementa el patrón Observer
 * Esta clase es el Sujeto, la que "Observa" que se haya producido un cambio
 * Y que se encarga de notificar a los observadores
 */
public interface Subject {

    /** Método que registra un objeto como observador
     * para saber a que objetos debe notificar
     * @param obj el objeto que se va a registrar como observador
     */
    void register(Observer obj);

    /** Método que elimina un objeto como observador
     * @param obj el objeto que se va a eliminar como observador (en el caso de que hubiera varios)
     */
    void unregister(Observer obj);

    /** Método que notifica a los observadores de que se ha producido un cambio
     * @throws Exception en el caso de que alguno de los observadores lance una excepción al actualizarse
     */
    //method to notify observers of change
    void notifyObservers() throws Exception;
}
