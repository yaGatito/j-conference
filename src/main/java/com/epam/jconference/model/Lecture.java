package com.epam.jconference.model;

import com.epam.jconference.model.enums.LectureStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@ToString
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
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User speaker;

    @Transient
    private String info;

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
        Lecture lecture = (Lecture) o;
        return id != null && Objects.equals(id, lecture.id);
    }
}
