package org.example.modelo.dao;

import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Clase con métodos genéricos para trabajar con la base de datos.
 */
public class MetodosGenerales {

    /** Método para obtener una lista de objetos de la base de datos.
     * @param query consulta a realizar
     * @return lista de objetos obtenidos
     * @throws Exception en caso de que no se pueda realizar la consulta
     */
    public static List obtenerLista(String query) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();

        TypedQuery<Object> consulta = em.createQuery(query, Object.class);
        List<Object> listaObjetos = consulta.getResultList();

        return listaObjetos;
    }
}
