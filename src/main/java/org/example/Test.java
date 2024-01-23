package org.example;

import org.example.modelo.Libro;
import org.example.modelo.dao.helper.Sql;
import org.example.singleton.Configuracion;

public class Test {
    public static void main(String[] args) {
        try {
            //Configuracion conf= Configuracion.getInstance();
            //conf.setPassword("admin1234");

            /*
            HibernateUtilJPA.getEntityManager();

            LibroDAOImpl lib = new LibroDAOImpl();
            List<Libro> libros = lib.leerAllLibros();
            System.out.println(libros);*/

            //Sql.importCSV(Libro.class,"Libro",',');

            //System.out.println(EncriptacionDesencriptacion.desencriptar("jL9QzuscGoBRMiEjVD0vHserm2Vm9F+3be04HP9Q7aZiYE1HRHXnKd8wMje+KXvX","asdf234fsdva%l9asdnklfa@f4f_adfafaAAaad;"));


            System.out.println("Hola Mundo");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
