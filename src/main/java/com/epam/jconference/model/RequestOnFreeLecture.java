package com.epam.jconference.model;

import com.epam.jconference.model.enums.RequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity(name = "RequestOnFreeLecture")
@NoArgsConstructor
@Table(name = "free_requests" ,uniqueConstraints = {
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
}
