package org.example.modelo.dao;

import org.example.modelo.Prestamo;
import org.example.modelo.dao.helper.LogFile;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.format.DateTimeFormatter;
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
    private static final String sqlINSERT="INSERT INTO prestamos (idLibro,idUsuario,fechaPrestamo) VALUES (?,?,?)";
    private static final String sqlUPDATE="UPDATE prestamos SET idLibro=?, idUsuario=?, fechaPrestamo=? WHERE idPrestamo = ?";
    private static final String sqlDELETE="DELETE FROM prestamos WHERE idPrestamo = ?";
    public PrestamoDAOImpl() {
    }
    @Override
    public boolean insertar(Prestamo prestamo) throws Exception {
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
            if(transaction!=null)
                transaction.rollback();
            throw e;
        }
        grabaEnLogIns(prestamo,sqlINSERT);
        return insertado;
    }
    private void grabaEnLogIns(Prestamo prestamo, String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdLibro()));
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdUsuario()));
        sql = sql.replaceFirst("\\?", prestamo.getFechaPrestamo().format(formatter));
        LogFile.saveLOG(sql);
    }

    @Override
    public boolean modificar(Prestamo prestamo) throws Exception {
        boolean actualizado = false;
        EntityManager em = HibernateUtilJPA.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Prestamo prestamo1 = em.find(Prestamo.class, prestamo.getId());
            prestamo1.setFechaPrestamo(prestamo.getFechaPrestamo());
            prestamo1.setIdLibro(prestamo.getIdLibro());
            prestamo1.setIdUsuario(prestamo.getIdUsuario());
            prestamo1.setIdPrestamo(prestamo.getIdPrestamo());
            em.merge(prestamo1);
            transaction.commit();
            actualizado = true;

        }catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
        grabaEnLogUpd(prestamo,sqlUPDATE); //Nose si hay que tocarlos ?
        return actualizado;
    }
    private void grabaEnLogUpd(Prestamo prestamo, String sql) throws Exception {
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdLibro()));
        sql = sql.replaceFirst("\\?",String.valueOf(prestamo.getIdUsuario()));
        sql = sql.replaceFirst("\\?", prestamo.getFechaPrestamo().format(formatter));
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
            Prestamo prestamo = em.find(Prestamo.class, id);

            if(prestamo != null){
                em.remove(prestamo);
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
    public List<Prestamo> leerAllPrestamos() throws Exception {
        return MetodosGenerales.obtenerLista("FROM Prestamo");
    }

    /**
     * Para instanciar un objeto préstamos a partir de un id
     * @param id clave primaria de la tabla préstamos
     * @return el objeto prestamos asociado a una clave primaria
     * @throws Exception cualquier error asociado a la consulta sql, grabar en fichero, ...
     */
    @Override
    public Prestamo getPrestamo(int id) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        return em.find(Prestamo.class, id);
    }
}