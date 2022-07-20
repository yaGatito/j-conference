package com.epam.jconference.model;

import com.epam.jconference.model.enums.RequestStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity(name = "RequestOnFreeLecture")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "free_requests", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"speaker_id", "free_lecture_id"})
})
public class RequestOnFreeLecture implements Serializable {

    @Id
    @ManyToOne
    private User speaker;

    @Id
    @ManyToOne
    private Lecture freeLecture;

    private RequestStatus status;

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
        RequestOnFreeLecture that = (RequestOnFreeLecture) o;
        return speaker != null && Objects.equals(speaker, that.speaker);
    }
}
