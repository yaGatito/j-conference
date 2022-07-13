package com.epam.jconference.repository;

import com.epam.jconference.model.Event;
import com.epam.jconference.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT e FROM EventListener el JOIN Event e ON el.event = e WHERE el.listener = :listener")
    List<Event> participation(@Param("listener") User listener);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e set e.lectures = " +
            "(SELECT COUNT(l) FROM Lecture l WHERE l.event = e AND l.status = 4) " +
            "WHERE e = :event")
    void updateQuantityOfLectures(@Param("event") Event event);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Event e set e.listeners = " +
            "(SELECT COUNT(l) FROM EventListener l WHERE l.event = e) " +
            "WHERE e = :event")
    void updateQuantityOfListeners(@Param("event") Event event);
}
