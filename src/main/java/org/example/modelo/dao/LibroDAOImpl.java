package org.example.modelo.dao;

import org.example.excepciones.CampoVacioExcepcion;
import org.example.modelo.Libro;
import org.example.modelo.dao.helper.LogFile;
import org.example.modelo.dao.helper.Sql;
import org.example.observer.Observer;
import org.example.observer.Subject;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.SQLException;
import java.util.List;

/**
 * Aquí implementaremos las reglas de negocio definidas
 * en la interfaz para trabajar con libro y  base de datos en MySQL
 * @author AGE
 * @version 3
 */
public class LibroDAOImpl implements LibroDAO, Subject {
    private static final String sqlINSERT="INSERT INTO libro (nombre,autor,editorial,categoria) VALUES (?,?,?,?)";
    private static final String sqlUPDATE="UPDATE libro SET nombre=?, autor=?, editorial=?, categoria=? WHERE id = ?";
    private static final String sqlDELETE="DELETE FROM libro WHERE id = ?";
    public LibroDAOImpl() {
    }

    @Override
    public boolean insertar(Libro libro) throws Exception {
        boolean insertado = false;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = null;
        try{
            transaction = em.getTransaction();
            transaction.begin();
            em.persist(libro);
            transaction.commit();
            insertado = true;
        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        notifyObservers();
        grabaEnLogIns(libro,sqlINSERT);
        return insertado;
    }

    private void grabaEnLogIns(Libro libro,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",libro.getNombre());
        sql = sql.replaceFirst("\\?",libro.getAutor());
        sql = sql.replaceFirst("\\?",libro.getEditorial());
        sql = sql.replaceFirst("\\?",String.valueOf(libro.getCategoria()));
        LogFile.saveLOG(sql);
    }

    @Override
    public boolean modificar(Libro libro) throws Exception {
        boolean actualizado = false;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Libro libro1 = em.find(Libro.class, libro.getId());
            libro1.setId(libro.getId());
            libro1.setCategoria(libro.getCategoria());
            libro1.setNombre(libro.getNombre());
            libro1.setAutor(libro.getAutor());
            libro1.setEditorial(libro.getEditorial());

            em.merge(libro1);
            transaction.commit();
            actualizado = true;

        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        notifyObservers();
        grabaEnLogUpd(libro,sqlUPDATE);
        return actualizado;
    }
    private void grabaEnLogUpd(Libro libro,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",libro.getNombre());
        sql = sql.replaceFirst("\\?",libro.getAutor());
        sql = sql.replaceFirst("\\?",libro.getEditorial());
        sql = sql.replaceFirst("\\?",String.valueOf(libro.getCategoria()));
        sql = sql.replaceFirst("\\?",String.valueOf(libro.getId()));
        LogFile.saveLOG(sql);
    }
    private void grabaEnLogDel(int id,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(id));
        LogFile.saveLOG(sql);
    }
    @Override
    public boolean borrar(int id) throws Exception {
        boolean borrado = false;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Libro libro = em.find(Libro.class, id);

            if(libro != null){
                em.remove(libro);
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
        grabaEnLogDel(id,sqlDELETE);
        // Al final de la operación, notificamos a los observadores
        notifyObservers();
        return borrado;
    }

    /**
     * Este método estático devuelve todos los libros de la BD,
     * este método tendremos en un futuro reimplmentarlo por rangos de x,
     * para que el rendimiento no decaiga cuando la tabla crezca
     * @return un arraylist con todos los libros de la BD
     * @throws SQLException cualquier error asociado a la consulta sql
     * @throws CampoVacioExcepcion en el caso que contenga una categoria con categoria a null
     */
    @Override
    public List<Libro> leerAllLibros() throws Exception {
        return (List<Libro>) MetodosGenerales.obtenerLista("FROM Libro");

    }

    //TODO cambiar documentación a Hibernate
    /**
    * Este método estático devuelve todos los libros de la BD,
    * que cumplan la condición según los parametros
    * este método tendremos en un futuro reimplmentarlo por rangos de x,
    * para que el rendimiento no decaiga cuando la tabla crezca
    * @param id código de libro a buscar
    * @param nombre búsqueda de libros con dicho título
    * @param autor búsqueda de libros con dicho autor
    * @param editorial búsqueda de libros con dicha editorial
    * @param categoria búsqueda de libros con dicho de código de categoría
    * @return un arraylist con todos los libros de la BD
    * @throws SQLException cualquier error asociado a la consulta sql
    * @throws CampoVacioExcepcion en el caso que contenga una categoria con categoria a null
    * */
    @Override
    public List<Libro> leerLibrosOR(int id, String nombre, String autor, String editorial, int categoria) throws Exception {
        String sql="SELECT l FROM Libro l";
        String where="";
        List<Libro> lista = null;
        EntityManager em = HibernateUtilJPA.getEntityManager();

        String wId="";
        if (id != 0) {
            wId = "l.id = :idLibro";
            where = Sql.rellenaWhereOR(where, wId);
        }
        String wNombre="";
        if (!nombre.trim().isEmpty()) {
            wNombre = "l.nombre LIKE :nombreLibro";
            where = Sql.rellenaWhereOR(where, wNombre);
        }
        String wAutor="";
        if (!autor.trim().isEmpty()) {
            wAutor = "l.autor LIKE :autorLibro";
            where = Sql.rellenaWhereOR(where, wAutor);
        }

        String wEditorial="";
        if (!editorial.trim().isEmpty()) {
            wEditorial = "l.editorial LIKE :editorialLibro";
            where = Sql.rellenaWhereOR(where, wEditorial);
            //where = id = ? OR nombre LIKE ? OR apellidos LIKE ?
        }

        String wCategoria="";
        if (categoria != 0) {
            wCategoria = "l.categoria = :categoriaLibro";
            where = Sql.rellenaWhereOR(where, wCategoria);
        }

        if (where.isEmpty())
            return leerAllLibros();
        else {
            sql = sql + " WHERE "+where;
            // sql = SELECT .... FROM usuario WHERE .......
            TypedQuery<Libro> typedQuery = em.createQuery(sql, Libro.class);

            if (!wId.isEmpty())
                typedQuery.setParameter("idLibro", id);
            if (!wNombre.isEmpty())
                typedQuery.setParameter("nombreLibro", nombre);
            if (!wAutor.isEmpty())
                typedQuery.setParameter("autorLibro", autor);
            if (!wEditorial.isEmpty())
                typedQuery.setParameter("editorialLibro", editorial);
            if (!wCategoria.isEmpty())
                typedQuery.setParameter("categoriaLibro", categoria);

            lista = typedQuery.getResultList();
        }
        LogFile.saveLOG(sql);
        return lista;
    }

    /**
     * para instanciar un objeto libro a partir de un id
     * @param id clave primaria de la tabla libro
     * @return el objeto libro asociado a una clave primaria
     * @throws SQLException cualquier error asociado a la consulta sql
     * @throws CampoVacioExcepcion en el caso que contenga una libro con libro a null

     */
    @Override
    public Libro getLibro(int id) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        return em.find(Libro.class, id);
    }

    private Observer observer;

    @Override
    public void register(Observer obj){
        if (obj == null) throw new NullPointerException("Null Observer");
        observer=obj;
    }

    @Override
    public void unregister(Observer obj) {
        observer=null;
    }

    @Override
    public void notifyObservers() throws Exception {
        if (observer!=null){
            observer.update(this);
        }
    }
}
