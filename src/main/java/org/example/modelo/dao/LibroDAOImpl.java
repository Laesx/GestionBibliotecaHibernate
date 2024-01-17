package org.example.modelo.dao;

import org.example.excepciones.CampoVacioExcepcion;
import org.example.modelo.Categoria;
import org.example.modelo.Libro;
import org.example.modelo.Usuario;
import org.example.modelo.dao.helper.LogFile;
import org.example.modelo.dao.helper.Sql;
import org.example.singleton.ConexionMySQL;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Aquí implementaremos las reglas de negocio definidas
 * en la interfaz para trabajar con libro y  base de datos en MySQL
 * @author AGE
 * @version 3
 */
public class LibroDAOImpl implements LibroDAO {
    private final Connection con;
    private static final String sqlINSERT="INSERT INTO libro (nombre,autor,editorial,categoria) VALUES (?,?,?,?)";
    private static final String sqlUPDATE="UPDATE libro SET nombre=?, autor=?, editorial=?, categoria=? WHERE id = ?";
    public LibroDAOImpl() throws Exception {
        con = ConexionMySQL.getInstance().getConexion();
    }

    @Override
    public boolean insertar(Libro libro) throws Exception {
        boolean insertado = false;

        EntityTransaction transaction = null;

        try{
            EntityManager em = HibernateUtilJPA.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(libro);

            transaction.commit();
            insertado = true;

        }catch (Exception e){
            e.printStackTrace(System.err);

            if(transaction!=null)
                transaction.rollback();
        }
        grabaEnLogIns(libro,sqlINSERT);

        /*
        try (PreparedStatement pstmt = con.prepareStatement(sqlINSERT,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,libro.getNombre());
            pstmt.setString(2,libro.getAutor());
            pstmt.setString(3,libro.getEditorial());
            pstmt.setInt(4,libro.getCategoria());
            insertado=pstmt.executeUpdate()==1;
            if (insertado) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next())
                    libro.setId(rs.getInt(1));
            }
        }
        grabaEnLogIns(libro,sqlINSERT);

         */
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

        }catch (Exception e){
            if(transaction.isActive())
                transaction.rollback();

            throw e;
        } finally {
            em.close();
        }
        /*
        try (PreparedStatement pstmt = con.prepareStatement(sqlUPDATE)){
            pstmt.setString(1,libro.getNombre());
            pstmt.setString(2,libro.getAutor());
            pstmt.setString(3,libro.getEditorial());
            pstmt.setInt(4, libro.getCategoria());
            pstmt.setInt(5, libro.getId());
            actualizado=pstmt.executeUpdate()==1;
        }

         */
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
            e.printStackTrace(System.err);
            if(transaction!= null){
                transaction.rollback();
            }
        }
        /*
        boolean borrado=false;
        try (PreparedStatement pstmt = con.prepareStatement(sqlDELETE)){
            pstmt.setInt(1, id);
            borrado=pstmt.executeUpdate()==1;
        }
        grabaEnLogDel(id,sqlDELETE);
        return borrado;

         */

        return true;
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
        /*
        List<Libro> lista = null;
        String sql="SELECT id,nombre,autor,editorial,categoria FROM libro";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            LogFile.saveLOG(sql);
            lista=new ArrayList<>();
            while (rs.next()){
                Libro libro=new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombre(rs.getString("nombre"));
                libro.setAutor(rs.getString("autor"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setCategoria(rs.getInt("categoria"));
                lista.add(libro);
            }
        }
        return lista;

         */

        return (List<Libro>) MetodosGenerales.obtenerLista("FROM Libro");

    }

    //TODO cambiar a Hibernate
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
        String sql="SELECT l.id, l.nombre, l.autor, l.editorial, l.categoria FROM Libro l";
        String where="";
        List<Libro> lista = null;
        EntityManager em = HibernateUtilJPA.getEntityManager();

        String wId="";
        if (id != 0) {
            wId = "l.id = :idLibro";
            where = Sql.rellenaWhereOR(where, wId);
            // where = "id = ?"
        }
        String wNombre="";
        if (!nombre.trim().isEmpty()) {
            wNombre = "l.nombre LIKE :nombreLibro";
            where = Sql.rellenaWhereOR(where, wNombre);
            //where = id = ? OR nombre LIKE ?
        }
        String wAutor="";
        if (!autor.trim().isEmpty()) {
            wAutor = "l.autor LIKE :autorLibro";
            where = Sql.rellenaWhereOR(where, wAutor);
            //where = id = ? OR nombre LIKE ? OR apellidos LIKE ?
        }

        String wEditorial="";
        if (!autor.trim().isEmpty()) {
            wEditorial = "l.editorial LIKE :editorialLibro";
            where = Sql.rellenaWhereOR(where, wEditorial);
            //where = id = ? OR nombre LIKE ? OR apellidos LIKE ?
        }

        String wCategoria="";
        if (!autor.trim().isEmpty()) {
            wCategoria = "l.categoria = :categoriaLibro";
            where = Sql.rellenaWhereOR(where, wCategoria);
            //where = id = ? OR nombre LIKE ? OR apellidos LIKE ?
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

        return lista;

        /*String sql="SELECT id,nombre,autor,editorial,categoria FROM libro";
        String where="";
        String wId="";
        if (id != 0) {
            wId = "id = ?";
            where = Sql.rellenaWhereOR(where, wId);
        }
        String wTitulo="";
        if (!titulo.trim().equals("")) {
            wTitulo = "nombre LIKE ?";
            where = Sql.rellenaWhereOR(where, wTitulo);
        }
        String wAutor="";
        if (!autor.trim().equals("")) {
            wAutor = "autor LIKE ?";
            where = Sql.rellenaWhereOR(where, wAutor);
        }
        String wEditorial="";
        if (!editorial.trim().equals("")) {
            wEditorial = "editorial LIKE ?";
            where = Sql.rellenaWhereOR(where, wEditorial);
        }
        String wCategoria="";
        if (categoria != 0) {
            wCategoria = "categoria = ?";
            where = Sql.rellenaWhereOR(where, wCategoria);
        }
        if (where.equals(""))
            return leerAllLibros();
        else {
            List<Libro> lista = null;
            sql = sql + " WHERE "+where;
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                int i = 1;
                if (!wId.equals(""))
                    pstmt.setInt(i++, id);
                if (!wTitulo.equals(""))
                    pstmt.setString(i++, titulo);
                if (!wAutor.equals(""))
                    pstmt.setString(i++, autor);
                if (!wEditorial.equals(""))
                    pstmt.setString(i++, editorial);
                if (!wCategoria.equals(""))
                    pstmt.setInt(i++, categoria);
                ResultSet rs = pstmt.executeQuery();
                LogFile.saveLOG(sql);
                lista=new ArrayList<>();
                while (rs.next()){
                    Libro libro=new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setNombre(rs.getString("nombre"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setEditorial(rs.getString("editorial"));
                    libro.setCategoria(rs.getInt("categoria"));
                    lista.add(libro);
                }
            }
            return lista;
        }

         */
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
        /*
        Libro libro=null;
        String sql="SELECT id,nombre,autor,editorial,categoria FROM libro WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            LogFile.saveLOG(sql.replace("?",String.valueOf(id)));
            if (rs.next()){
                libro=new Libro();
                libro.setId(rs.getInt("id"));
                libro.setNombre(rs.getString("nombre"));
                libro.setAutor(rs.getString("autor"));
                libro.setEditorial(rs.getString("editorial"));
                libro.setCategoria(rs.getInt("categoria"));
            }
        }
        return libro;

         */
    }
}
