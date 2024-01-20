package org.example.modelo.dao.helper;

import org.example.singleton.HibernateUtilJPA;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Clase auxiliar con distintas funcionalidades a la hora de trabajar con SQL
 * @author AGE
 * @version 2
 */
public class Sql {
    /**
     * Prepara la parte where de una cláusula para concatenar condiciones usando el OR
     * @param where parte where actual antes de su ejecución
     * @param opcion condición a incluir en el futuro where
     * @return devuelve la parte where junto con la condición enviada y concatenada según una OR
     */
    public static String rellenaWhereOR(String where, String opcion) {
        if (!opcion.equals("")){
            if (where.equals(""))
                where=opcion;
            else where+=" OR "+opcion;
        }
        return where;
    }
    /**
     * Prepara la parte where de una cláusula para concatenar condiciones usando el AND
     * @param where parte where actual antes de su ejecución
     * @param opcion condición a incluir en el futuro where
     * @return devuelve la parte where junto con la condición enviada y concatenada según una AND
     */
    public static String rellenaWhereAND(String where, String opcion) {
        if (!opcion.equals("")){
            if (where.equals(""))
                where=opcion;
            else where+=" AND "+opcion;
        }
        return where;
    }
    /*
    public static void importCSV(Path path, String tabla,char delimiter) throws Exception {
        String sql = "SELECT * FROM "+tabla;
        Connection con = ConexionMySQL.getInstance().getConexion();
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            LogFile.saveLOG(sql);
            // escribimos la linea de la cabecera
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            String cabecera="";
            int numColumn=resultSetMetaData.getColumnCount();
            for (int i=1;i<=numColumn;i++) {
                cabecera+=resultSetMetaData.getColumnName(i);
                if (i!=numColumn)
                    cabecera+=delimiter;
                else cabecera+='\n';
            }
            Files.writeString(path,cabecera, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            while (rs.next()){
                String fila="";
                for (int i=1; i<=numColumn;i++) {
                    fila+=rs.getObject(i).toString();
                    if (i!=numColumn)
                        fila+=delimiter;
                    else fila+='\n';
                }
                Files.writeString(path,fila, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            }
        }
    }*/

    public static <T> void importCSV(Class<T> entityClass, String tabla, char delimiter) throws Exception {
        EntityManager em = HibernateUtilJPA.getEntityManager();
        String sql = "SELECT e FROM " + tabla + " e";
        EntityType<T> entityType = em.getMetamodel().entity(entityClass);

        List<T> results = em.createQuery(sql, entityClass).getResultList();
        Path path = Paths.get(tabla + ".csv");
        if (!results.isEmpty()) {
            StringBuilder cabecera = new StringBuilder();
            for (Attribute<?, ?> attribute : entityType.getAttributes()) {
                cabecera.append(attribute.getJavaMember().getName());
                cabecera.append(delimiter);
            }
            cabecera.append('\n');
            Files.writeString(path, cabecera.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            for (T row : results) {
                StringBuilder fila = new StringBuilder();
                for (Attribute<?, ?> attribute : entityType.getAttributes()) {
                    fila.append(entityType.getJavaType().getMethod("get" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1)).invoke(row));
                    fila.append(delimiter);
                }
                fila.append('\n');
                Files.writeString(path, fila.toString(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            }
        }
    }
}
