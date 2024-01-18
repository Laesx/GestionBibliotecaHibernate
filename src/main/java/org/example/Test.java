package org.example;

import org.example.singleton.Configuracion;
import org.example.singleton.HibernateUtilJPA;

public class Test {
    public static void main(String[] args) {
        try {
            Configuracion conf= Configuracion.getInstance();
            //conf.setPassword("admin1234");

            HibernateUtilJPA.getEntityManager();

            //System.out.println(EncriptacionDesencriptacion.desencriptar("jL9QzuscGoBRMiEjVD0vHserm2Vm9F+3be04HP9Q7aZiYE1HRHXnKd8wMje+KXvX","asdf234fsdva%l9asdnklfa@f4f_adfafaAAaad;"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
