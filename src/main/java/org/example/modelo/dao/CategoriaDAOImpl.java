package org.example.modelo.dao;

import org.example.excepciones.CampoVacioExcepcion;
import org.example.modelo.dao.helper.LogFile;
import org.example.singleton.ConexionMySQL;
import org.example.modeloJPA.Categoria;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Aquí implementaremos las reglas de negocio definidas
 * en la interfaz para trabajar con categorías y
 * base de datos en MySQL
 * @author AGE
 * @version 2
 */
public class CategoriaDAOImpl implements CategoriaDAO {

    //private final Connection con;
    private static final String sqlINSERT="INSERT INTO categoria (categoria) VALUES (?)";
    public CategoriaDAOImpl() throws Exception {
        //con = ConexionMySQL.getInstance().getConexion();
    }

    @Override
    public boolean inserta(Categoria categoria) throws Exception {
        /*
        boolean insertado;
        try (PreparedStatement pstmt = con.prepareStatement(sqlINSERT,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,categoria.getCategoria());
            insertado=pstmt.executeUpdate()==1;
            if (insertado) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next())
                    categoria.setId(rs.getInt(1));
            }

        }
        LogFile.saveLOG(sqlINSERT.replace("?",categoria.getCategoria()));
        return insertado;
        */

        boolean insertado = false;
        EntityTransaction transaction = null;

        try{
            EntityManager em = HibernateUtilJPA.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(categoria);

            transaction.commit();
            insertado = true;

        }catch (Exception e){
            e.printStackTrace(System.err);

            if(transaction!=null)
                transaction.rollback();
        }
        LogFile.saveLOG(sqlINSERT.replace("?",categoria.getCategoria()));
        return insertado;
    }

    @Override
    public boolean modificar(Categoria categoria) throws Exception {
        /*
        boolean actualizado;
        try (PreparedStatement pstmt = con.prepareStatement(sqlUPDATE)) {
            pstmt.setString(1,categoria.getCategoria());
            pstmt.setInt(2,categoria.getId());
            actualizado=pstmt.executeUpdate()==1;
        }
        String sql=sqlUPDATE.replaceFirst("\\?",
                categoria.getCategoria());
        LogFile.saveLOG(sql.replace("?",String.valueOf(categoria.getId())));
        return actualizado;

         */

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

        }catch (Exception e){
            if(transaction.isActive())
                transaction.rollback();

            throw e;
        } finally {
            em.close();
        }

        return modificado;
    }

    @Override
    public boolean borrar(int id) throws Exception {
        /*
        boolean borrado=false;
        try (PreparedStatement pstmt = con.prepareStatement(sqlDELETE)) {
            pstmt.setInt(1,id);
            borrado=pstmt.executeUpdate()==1;
        }
        LogFile.saveLOG(sqlDELETE.replace("?",String.valueOf(id)));
        return borrado;
        */


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
            e.printStackTrace(System.err);
            if(transaction!= null){
                transaction.rollback();
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        Connection con = ConexionMySQL.getInstance().getConexion();
        int maximo= 0;
        String sql = "SELECT MAX(id) AS max_id FROM categoria";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            LogFile.saveLOG(sql);
            if (rs.next()) {
                maximo= rs.getInt("max_id");
            }
        }

         */
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
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        Connection con = ConexionMySQL.getInstance().getConexion();
        int minimo= 0;
        String sql = "SELECT MIN(id) AS min_id FROM categoria";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            LogFile.saveLOG(sql);
            if (rs.next()) {
                minimo= rs.getInt("min_id");
            }
        }
        return minimo;

         */

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
        /*
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            LogFile.saveLOG(sql.replace("?",String.valueOf(id)));
            if (rs.next()){
                categoria=new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setCategoria(rs.getString("Categoria"));
            }
        }

         */
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
        /*
        List<Categoria> lista = null;
        String sql = "SELECT id,categoria FROM categoria";

        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            LogFile.saveLOG(sql);
            lista = new ArrayList<>();
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getInt("id"));
                categoria.setCategoria(rs.getString("categoria"));
                lista.add(categoria);
            }
        }
        return lista;

         */

        return (List<Categoria>) MetodosGenerales.obtenerLista("FROM Categoria");
    }



}
