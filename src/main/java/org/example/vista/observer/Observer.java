package org.example.vista.observer;

public interface Observer {
    //method to update the observer, used by subject
    public void update(Subject sub) throws Exception;
}
