package com.epam.jconference.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "event_tags", uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "tags_id"})})
public class EventTag implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tags_id", nullable = false)
    private Tag tags;
}
