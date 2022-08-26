package com.epam.jconference.repository.impl;

import com.epam.jconference.model.EventListener;
import com.epam.jconference.repository.EventListenerRepository;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
public class EventListenerRepositoryImpl implements EventListenerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void add(EventListener eventListener) {
        entityManager.createNativeQuery("INSERT INTO event_listeners (listeners_id, event_id) VALUES (?1, ?2);")
                .setParameter(1, eventListener.getListener().getId())
                .setParameter(2, eventListener.getEvent().getId())
                .executeUpdate();
    }

    public boolean exists(EventListener eventListener) {
        return entityManager.createNativeQuery("SELECT * FROM event_listeners WHERE event_id = ? AND listeners_id = ?")
                .setParameter(1, eventListener.getEvent().getId())
                .setParameter(2, eventListener.getListener().getId())
                .getResultList().size() > 0;
    }

    @Transactional
    public void remove(EventListener eventListener) {
        entityManager.createNativeQuery("DELETE FROM event_listeners WHERE listeners_id = ?1 AND event_id =?2;")
                .setParameter(1, eventListener.getListener().getId())
                .setParameter(2, eventListener.getEvent().getId())
                .executeUpdate();
    }
}
