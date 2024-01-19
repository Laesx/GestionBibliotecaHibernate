package org.example.modelo.dao;

import org.example.modelo.Historico;
import org.example.modelo.dao.helper.LogFile;
import org.example.singleton.ConexionMySQL;
import org.example.singleton.Configuracion;
import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * Aquí implementaremos las reglas de negocio definidas
 * en la interfaz para trabajar con historico y
 * base de datos en MySQL
 * @author AGE
 * @version 2
 */
public class HistoricoDAOImpl implements HistoricoDAO {
    private Historico historico;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Connection con;
    private static final String sqlINSERT="INSERT INTO historico (user,fecha,info) VALUES (?,?,?)";
    public HistoricoDAOImpl(Historico historico) throws Exception {
        this.historico = historico;
        con = ConexionMySQL.getInstance().getConexion();
    }


    @Override
    public Historico getHistorico() {
        return historico;
    }

    @Override
    public boolean insertar() throws SQLException, IOException {
        boolean insertado = false;

        EntityTransaction transaction = null;

        try{
            EntityManager em = HibernateUtilJPA.getEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            em.persist(historico);

            transaction.commit();
            insertado = true;

        }catch (Exception e){
            e.printStackTrace(System.err);

            if(transaction!=null)
                transaction.rollback();
        }

        return insertado;
    }

    private void grabaEnLogIns(String sql) throws IOException {
        sql = sql.replaceFirst("\\?", String.valueOf(historico.getUser()));
        sql = sql.replaceFirst("\\?", historico.getFecha().format(formatter));
        sql = sql.replaceFirst("\\?", String.valueOf(historico.getInfo()));
        LogFile.saveLOGsinBD(sql);
    }

    /**
     * Este metodo inserta aquellos mensajes que son enviados al fichero LOG
     * pero que serán almacenados en la tabla Historico
     * @param msgLog mensaje enviado para añadir a la tabla Historico
     * @throws IOException
     * @throws SQLException
     */
    public static void mensaje(String msgLog) throws Exception {
        Historico historico=new Historico();
        historico.setUser(Configuracion.getInstance().getUser());
        historico.setInfo(msgLog);
        new HistoricoDAOImpl(historico).insertar();
    }

}