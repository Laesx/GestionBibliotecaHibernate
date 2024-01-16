package org.example.singleton;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;

import java.util.logging.Level;

public class HibernateUtil {
    //TODO Borrar si no se usa al final
    private HibernateUtil() {
    }

    private static SessionFactory sessionFactory=null;

    public static SessionFactory getSessionFactory(boolean showLog) {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory(showLog);
            Runtime.getRuntime().addShutdownHook(new ThreadOff());
        }
        return sessionFactory;
    }
    private static void showLog(boolean showLog) {
        if (!showLog) {
            // Para eliminar los mensajes excesivos de Hibernate, hacer cuando esté funcionado bien.
            //Logger hibernateLogger = Logger.getLogger("org.hibernate");
            //hibernateLogger.setLevel(Level.SEVERE); // Configurar el nivel para que solo muestre errores "severos"

            // Para eliminar los mensajes de Hibernate/ hacer cuando este funcionado bien
            @SuppressWarnings("unused")
            org.jboss.logging.Logger logger = org.jboss.logging.Logger.getLogger("org.hibernate");
            java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        }

    }
    private static SessionFactory buildSessionFactory(boolean show) {
        try {
            // Crea la configuración desde hibernate.cfg.xml
            showLog(show);
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            return configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al construir SessionFactory: " + e.getMessage(), e);
        }
    }

    private static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
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
        if (th instanceof ConstraintViolationException) {
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
            closeSessionFactory();
        }
    }
}
