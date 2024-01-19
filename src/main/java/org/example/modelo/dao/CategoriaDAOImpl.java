package org.example.modelo.dao;

import org.example.excepciones.CampoVacioExcepcion;
import org.example.modelo.Categoria;
import org.example.modelo.dao.helper.LogFile;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.List;
/**
 * Aquí implementaremos las reglas de negocio definidas
 * en la interfaz para trabajar con categorías y
 * base de datos en MySQL
 * @author AGE
 * @version 2
 */
public class CategoriaDAOImpl implements CategoriaDAO {
    private static final String sqlINSERT="INSERT INTO categoria (categoria) VALUES (?)";
    public CategoriaDAOImpl(){
    }

    @Override
    public boolean inserta(Categoria categoria) throws Exception {
        boolean insertado = false;
        EntityTransaction transaction = null;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        try{
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(categoria);
            transaction.commit();
            insertado = true;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        LogFile.saveLOG(sqlINSERT.replace("?",categoria.getCategoria()));
        return insertado;
    }

    @Override
    public boolean modificar(Categoria categoria) throws Exception {
        boolean modificado = false;

        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            Categoria categoria1 = em.find(Categoria.class, categoria.getId());
            categoria1.setCategoria(categoria.getCategoria());
            em.merge(categoria1);
            transaction.commit();
            modificado = true;
        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }

        return modificado;
    }

    @Override
    public boolean borrar(int id) throws Exception {
        boolean borrado = false;

        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{
            transaction.begin();
            Categoria categoria = em.find(Categoria.class, id);

            if(categoria != null){
                em.remove(categoria);
                transaction.commit();
                borrado = true;
            }
        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }

        return borrado;
    }
    /**
     * el valor máximo del campo id de la tabla categorías
     * @return valor máximo del campo id
     * @throws Exception cualquier error asociado a la consulta sql
     */
    public static int maximaId() throws Exception {

        int maximo = 0;
        EntityManager em = HibernateUtilJPA.getEntityManager();

        try{
            String sql = "SELECT MAX(id) FROM Categoria ";
            Query query = em.createQuery(sql);

            Object resultado = query.getSingleResult();

            if(resultado != null){
                maximo = ((Number) resultado).intValue();
            }
        } finally {
            em.close();
        }

        return maximo;


    }

    /**
     * el valor mínimo del campo id de la tabla categorías
     * @return valor mínimo del campo id
     * @throws Exception cualquier error asociado a la consulta sql
     */
    public static int minimaId() throws Exception {
        int minimo = 0;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        try{
            String sql = "SELECT MIN(id) FROM Categoria ";
            Query query = em.createQuery(sql);
            Object resultado = query.getSingleResult();
            if(resultado != null){
                minimo = ((Number) resultado).intValue();
            }
        } finally {
            em.close();
        }
        return minimo;
    }

    /**
     * para instanciar un objeto categoria a partir de un id
     * @param id clave primaria de la tabla categoria
     * @return el objeto categoría asociado a una clave primaria
     * @throws Exception cualquier error asociado a la consulta sql
     */
    @Override
    public Categoria categoria(int id) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        return em.find(Categoria.class, id);
    }


    /**
     * Este método estático devuelve todos las categorías de la BD,
     * este método tendremos en un futuro reimplmentarlo por rangos de x,
     * para que el rendimiento no decaiga cuando la tabla crezca
     * @return un arraylist con todos las categorias de la BD
     * @throws SQLException cualquier error asociado a la consulta sql
     * @throws CampoVacioExcepcion en el caso que contenga una categoria con categoria a null
     */
    public List<Categoria> leerAllCategorias() throws Exception {
        return (List<Categoria>) MetodosGenerales.obtenerLista("FROM Categoria");
    }



}
