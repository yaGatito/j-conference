package com.epam.jconference.model;

import com.epam.jconference.model.enums.LectureStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedQuery(name = "Lecture.existsFreeLecture",
        query = "SELECT COUNT(t) FROM Lecture t WHERE t.speaker IS NULL AND t.event = :event AND t.topic = :topic")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_speaker_event_topic",
                columnNames = {"speaker_id", "event_id", "topic"}
        )
})
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private LectureStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private User speaker;

    @Transient
    private String info;
}
