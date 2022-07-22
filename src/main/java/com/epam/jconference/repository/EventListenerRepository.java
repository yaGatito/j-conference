package com.epam.jconference.repository;

import com.epam.jconference.model.EventListener;

public interface EventListenerRepository {

    void add(EventListener eventListener);

    boolean exists(EventListener eventListener);

    void remove(EventListener eventListener);
}
