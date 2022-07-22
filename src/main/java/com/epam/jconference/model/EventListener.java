package com.epam.jconference.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "event_listeners",
        uniqueConstraints = {@UniqueConstraint(name = "UniqueEventAndListener",
                columnNames = {"event_id", "listeners_id"})})
@AllArgsConstructor
@NoArgsConstructor
public class EventListener implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    @ToString.Exclude
    private Event event;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listeners_id", nullable = false)
    @ToString.Exclude
    private User listener;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        EventListener that = (EventListener) o;
        return event != null && Objects.equals(event, that.event);
    }
}
