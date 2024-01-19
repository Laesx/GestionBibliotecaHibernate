package org.example.modelo.dao;

import org.example.modelo.Categoria;
import org.example.modelo.Libro;
import org.example.modelo.Prestamo;
import org.example.modelo.Usuario;
import org.example.modelo.dao.helper.LogFile;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class MetodosGenerales {

    public static List obtenerLista(String query) throws Exception {
        String listaImpresion=null;
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
        Class<?> clase= listaObjetos.get(0).getClass();
        if(clase.equals(Categoria.class)){
            listaImpresion = "SELECT * FROM categoria";
        } else if (clase.equals(Libro.class)) {
            listaImpresion = "SELECT * FROM libro";
        } else if (clase.equals(Prestamo.class)) {
            listaImpresion = "SELECT * FROM prestamos";
        } else if (clase.equals(Usuario.class)) {
            listaImpresion = "SELECT * FROM usuario";
        }
        LogFile.saveLOG(listaImpresion);
        return listaObjetos;
    }
}
