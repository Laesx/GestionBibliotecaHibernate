package org.example;

import org.example.modelo.Libro;
import org.example.modelo.dao.LibroDAOImpl;
import org.example.singleton.Configuracion;
import org.example.singleton.HibernateUtilJPA;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        try {
            Configuracion conf= Configuracion.getInstance();
            //conf.setPassword("admin1234");

            HibernateUtilJPA.getEntityManager();

            LibroDAOImpl lib = new LibroDAOImpl();
            List<Libro> libros = lib.leerAllLibros();
            System.out.println(libros);

            //System.out.println(EncriptacionDesencriptacion.desencriptar("jL9QzuscGoBRMiEjVD0vHserm2Vm9F+3be04HP9Q7aZiYE1HRHXnKd8wMje+KXvX","asdf234fsdva%l9asdnklfa@f4f_adfafaAAaad;"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
