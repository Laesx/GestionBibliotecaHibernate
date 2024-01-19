package org.example.observer;

public interface Observer {
    //method to update the observer, used by subject
    public void update(Subject subject) throws Exception;
}
