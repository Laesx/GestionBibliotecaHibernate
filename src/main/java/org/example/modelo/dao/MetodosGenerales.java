package org.example.modelo.dao;

import org.example.singleton.HibernateUtilJPA;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.lang.reflect.Type;
import java.util.List;

public class MetodosGenerales {

    public static EntityManagerFactory entityManagerFactory(){
        EntityManagerFactory em;
        em = HibernateUtilJPA.getEntityManagerFactory();

        return em;
    }

    public static List obtenerLista(String query){
        EntityManager em = entityManagerFactory().createEntityManager();
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
