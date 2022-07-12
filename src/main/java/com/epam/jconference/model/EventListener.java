package com.epam.jconference.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "event_listeners", uniqueConstraints = {@UniqueConstraint(name = "UniqueEventAndListener", columnNames = {"event_id", "listeners_id"})})
@AllArgsConstructor
@NoArgsConstructor
public class EventListener implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listeners_id", nullable = false)
    private User listener;
}
