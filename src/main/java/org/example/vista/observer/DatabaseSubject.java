package org.example.vista.observer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;

public class DatabaseSubject {
    private ArrayList<DatabaseChangeObserver> observers = new ArrayList<>();

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

    public void addObserver(DatabaseChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(DatabaseChangeObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String eventData) {
        for (DatabaseChangeObserver observer : observers) {
            observer.update(eventData);
        }
    }

    public void simulateDatabaseEvent(String eventData) {
        System.out.println("Simulating database event...");
        notifyObservers(eventData);

        // Optionally, save the event to the database using JPA
        saveEventToDatabase(eventData);
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
}

