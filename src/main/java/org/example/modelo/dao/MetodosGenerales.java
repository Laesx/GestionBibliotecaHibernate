package org.example.modelo.dao;

import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class MetodosGenerales {

    public static List obtenerLista(String query) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        List<Object> listaObjetos = null;

        try{
            TypedQuery<Object> consulta = em.createQuery(query, Object.class);
            listaObjetos = consulta.getResultList();
        }  catch(Exception e){
            e.printStackTrace();
        } finally {
            em.close();
        }

        return listaObjetos;
    }
}
