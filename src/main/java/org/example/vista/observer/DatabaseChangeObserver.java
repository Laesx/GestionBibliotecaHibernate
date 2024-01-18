package org.example.vista.observer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseChangeObserver implements Observer {
    private static final String PERSISTENCE_UNIT_NAME = "your_persistence_unit_name";
    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            // Initialize JPA EntityManagerFactory
            entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }


    public void update(String eventData) {
        // Implement the logic to handle the database event
        System.out.println("Database event received: " + eventData);

        // Add your custom logic here
        // For example, you could log the event to a file, send a notification, etc.

        // Optionally, save the event to the database using JPA
        saveEventToDatabase(eventData);
    }

    @Override
    public void update() throws Exception {

    }

    private void saveEventToDatabase(String eventData) {
        EntityManager entityManager = null;

        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            /*
            EventLog eventLog = new EventLog();
            eventLog.setEventData(eventData);

            entityManager.persist(eventLog);*/

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void setSubject(Subject sub) {

    }
}

