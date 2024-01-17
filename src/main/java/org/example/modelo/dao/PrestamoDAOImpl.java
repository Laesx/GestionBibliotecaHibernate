package org.example.modelo.dao;

import org.example.modelo.Libro;
import org.example.modelo.dao.helper.LogFile;
import org.example.modeloJPA.Prestamos;
import org.example.singleton.ConexionMySQL;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Aquí implementaremos las reglas de negocio definidas
 * en la interfaz para trabajar con préstamos y
 * base de datos en MySQL
 * @author AGE
 * @version 2
 */
public class PrestamoDAOImpl implements PrestamoDAO {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Connection con;
    private static final String sqlINSERT="INSERT INTO prestamos (idLibro,idUsuario,fechaPrestamo) VALUES (?,?,?)";
    private static final String sqlUPDATE="UPDATE prestamos SET idLibro=?, idUsuario=?, fechaPrestamo=? WHERE idPrestamo = ?";
    private static final String sqlDELETE="DELETE FROM prestamos WHERE idPrestamo = ?";
    public PrestamoDAOImpl() throws Exception {
        con = ConexionMySQL.getInstance().getConexion();
    }
    @Override
    public boolean insertar(Prestamos prestamo) throws Exception {
        boolean insertado=false;
        EntityTransaction transaction = null;

        try{
            EntityManager em = HibernateUtilJPA.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(prestamo);

            transaction.commit();
            insertado = true;

        }catch (Exception e){
            e.printStackTrace(System.err);

            if(transaction!=null)
                transaction.rollback();
        }
        /*
        try(PreparedStatement pstmt =con.prepareStatement(sqlINSERT,PreparedStatement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, prestamo.getIdLibro());
            pstmt.setInt(2, prestamo.getIdUsuario());
            pstmt.setString(3, prestamo.getFechaPrestamo().format(formatter));
            insertado=(pstmt.executeUpdate()==1);
            if (insertado) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next())
                    prestamo.setIdPrestamo(rs.getInt(1));
            }
        }

         */
        grabaEnLogIns(prestamo,sqlINSERT);
        return insertado;
    }
    private void grabaEnLogIns(Prestamos prestamo,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdLibro()));
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdUsuario()));
        sql = sql.replaceFirst("\\?",prestamo.getFechaPrestamo().toString());
        LogFile.saveLOG(sql);
    }

    @Override
    public boolean modificar(Prestamos prestamo) throws Exception {
        boolean actualizado;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();


        try {
            transaction.begin();
            Prestamos prestamo1 = em.find(Prestamos.class, prestamo.getIdPrestamo());
            prestamo1.setFechaPrestamo(prestamo.getFechaPrestamo());
            prestamo1.setIdLibro(prestamo.getIdLibro());
            prestamo1.setIdUsuario(prestamo.getIdUsuario());
            prestamo1.setIdPrestamo(prestamo.getIdPrestamo());

            em.merge(prestamo1);
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
            pstmt.setInt(1, prestamo.getIdLibro());
            pstmt.setInt(2, prestamo.getIdUsuario());
            pstmt.setString(3, prestamo.getFechaPrestamo().format(formatter));
            pstmt.setInt(4, prestamo.getIdPrestamo());
            actualizado=pstmt.executeUpdate()==1;
            grabaEnLogUpd(prestamo,sqlUPDATE);
        }

         */
        grabaEnLogUpd(prestamo,sqlUPDATE); //Nose si hay que tocarlos ?
        return actualizado;
    }
    private void grabaEnLogUpd(Prestamos prestamo,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdLibro()));
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdUsuario()));
        sql = sql.replaceFirst("\\?",prestamo.getFechaPrestamo().toString());
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdPrestamo()));
        LogFile.saveLOG(sql);
    }

    @Override
    public boolean borrar(int id) throws Exception {
        boolean borrado=false;

        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{
            transaction.begin();
            Prestamos prestamo = em.find(Prestamos.class, id);

            if(prestamo != null){
                em.remove(prestamo);
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
        try (PreparedStatement pstmt = con.prepareStatement(sqlDELETE)){
            pstmt.setInt(1, id);
            borrado=pstmt.executeUpdate()==1;
        }

         */
        grabaEnLog(id,sqlDELETE);
        return borrado;
    }
    private void grabaEnLog(int id,String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(id));
        LogFile.saveLOG(sql);
    }

    /**
     * Este método estático devuelve todos los prestamoss de la BD,
     * este método tendremos en un futuro reimplmentarlo por rangos de x,
     * para que el rendimiento no decaiga cuando la tabla crezca
     * @return un arraylist con todos los prestamos de la BD
     * @throws Exception cualquier error asociado a la consulta sql, grabar en fichero...
     */
    @Override
    public List<Prestamos> leerAllPrestamos() throws Exception {
        return MetodosGenerales.obtenerLista("FROM Prestamo");
        /*
        List<Prestamo> lista = null;
        String sql="SELECT idPrestamo,idLibro,idUsuario,fechaPrestamo FROM prestamos";
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            LogFile.saveLOG(sql);
            lista=new ArrayList<>();
            while (rs.next()){
                Prestamo prestamo =new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("idPrestamo"));
                prestamo.setIdLibro(rs.getInt("idLibro"));
                prestamo.setIdUsuario(rs.getInt("idUsuario"));
                prestamo.setFechaPrestamo(LocalDateTime.parse(rs.getString("fechaPrestamo"), formatter));
                lista.add(prestamo);
            }
        }
        return lista;

         */
    }

    /**
     * Para instanciar un objeto préstamos a partir de un id
     * @param id clave primaria de la tabla préstamos
     * @return el objeto prestamos asociado a una clave primaria
     * @throws Exception cualquier error asociado a la consulta sql, grabar en fichero, ...
     */
    @Override
    public Prestamos getPrestamo(int id) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        return em.find(Prestamos.class, id);
        /*
        Prestamo prestamo =null;
        String sql="SELECT idPrestamo,idLibro,idUsuario,fechaPrestamo FROM prestamos WHERE idPrestamo = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            LogFile.saveLOG(sql.replace("?",String.valueOf(id)));
            if (rs.next()){
                prestamo =new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("idPrestamo"));
                prestamo.setIdLibro(rs.getInt("idLibro"));
                prestamo.setIdUsuario(rs.getInt("idUsuario"));
                prestamo.setFechaPrestamo(LocalDateTime.parse(rs.getString("fechaPrestamo"), formatter));
            }
        }

         */
    }
}