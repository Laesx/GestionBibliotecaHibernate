package org.example.modelo.dao;

import org.example.modeloJPA.Prestamos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
/**
 * Esta será la intefaz para manejar los préstamos de la BD BIBLIOTECA
 * @author AGE
 * @version 2
 */
public interface PrestamoDAO {

    /**
     * Implementaremos las instrucciones necesarias para poder
     * insertar un registro asociado a la tabla prestamos para algún
     * sistema gestor de BD o de ficheros
     * @return verdad en el caso de que la inserción ser realice con éxito
     * @throws SQLException
     * @throws IOException
     */
    boolean insertar(Prestamos prestamo) throws Exception;

    /**
     * Implementaremos las instrucciones necesarias para poder
     * modificar un registro asociado a la tabla prestamos para algún
     * sistema gestor de BD o de ficheros
     * @return verdad en el caso de que la modificación concluya éxito
     * @throws SQLException
     * @throws IOException
     */
    boolean modificar(Prestamos prestamo) throws Exception;

    /**
     * Implementaremos las instrucciones necesarias para poder
     * borrar un registro asociado a la tabla prestamos para algún
     * sistema gestor de BD o de ficheros
     * @return verdad en el caso de que el borrado concluya éxito
     * @throws SQLException
     * @throws IOException
     */
    boolean borrar(int id) throws Exception;

    List<Prestamos> leerAllPrestamos() throws Exception;

    Prestamos getPrestamo(int id) throws Exception;
}
