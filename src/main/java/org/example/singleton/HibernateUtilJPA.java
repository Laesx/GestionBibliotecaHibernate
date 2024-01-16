package org.example.singleton;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class HibernateUtilJPA {
    // Reemplaza con el nombre de tu unidad de persistencia ver en archivo persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "biblioteca";
    private static final boolean showLog=false;
    private HibernateUtilJPA() {

    }

    private static void showLog() {
        if (!showLog) {
            // Para eliminar los mensajes de Hibernate/ hacer cuando este funcionado bien
            @SuppressWarnings("unused")
            org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        }

    }
    private static EntityManagerFactory entityManagerFactory=null;
    public static EntityManager getEntityManager(){
        getEntityManagerFactory();
        EntityManager em=null;
        if (entityManagerFactory!=null)
            em=entityManagerFactory.createEntityManager();
        return em;
    }
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = createEntityManagerFactory();
            Runtime.getRuntime().addShutdownHook(new ThreadOff());
        }
        return entityManagerFactory;
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        showLog();
        // por si no está configurado en persistence.xml
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"); // Reemplaza con el dialecto de tu base de datos
        // Puedes agregar más propiedades según tus necesidades
        return new HibernatePersistenceProvider()
                .createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);

        //return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // solo mirando el fichero persistence.xml
    }

    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
    public static void printException(Exception e){
        System.err.println("---- Pila de excepciones INICIO ----");
        System.err.println("[superior]" + e.getClass().toString());
        Throwable th = e;
        int i=1;
        while ((th != null) && !(th instanceof ConstraintViolationException)) {
            th = th.getCause();
            if (th != null)
                System.err.println("[-" + (i++) + "]" + th.getClass().toString());
        }
        System.err.println("---- Pila de excepciones: FIN   ----");
        if (th !=null && th instanceof ConstraintViolationException ) {
            ConstraintViolationException cve = (ConstraintViolationException) th;
            System.err.println("===================================");
            System.err.println("Excepción de Hibernate de tipo " + cve.getClass().getName() + ": [" + cve.getMessage() + "]");
            System.err.println("Sentencia SQL: " + cve.getSQL());
            System.err.println("Restricción violada: " + cve.getConstraintName());
            System.err.print("Error de la excepción SQLException: ");
            System.err.println(cve.getSQLException().getMessage());
            System.err.println("===================================");
        } else {
            e.printStackTrace(System.err);
        }
    }

    private static class ThreadOff extends Thread {
        @Override
        public void run() {
            super.run();
            closeEntityManagerFactory();
        }
    }
}