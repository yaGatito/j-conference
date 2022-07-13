package com.epam.jconference.repository;

import com.epam.jconference.model.Event;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;
import com.epam.jconference.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    Integer existsFreeLecture(@Param("event") Event event, @Param("topic") String topic);

    List<Lecture> findAllByStatus(LectureStatus lectureStatus);

    List<Lecture> findAllByStatusAndSpeaker(LectureStatus status, User speaker);

    @Query(value = "SELECT l FROM Lecture l JOIN RequestOnFreeLecture r ON l = r.freeLecture WHERE r.status = :status")
    List<Lecture> freeLecturesByRequestStatus(@Param("status") RequestStatus status);

    @Query(value = "SELECT l FROM Lecture l JOIN RequestOnFreeLecture r ON l = r.freeLecture WHERE r.status = :status and r.speaker = :speaker")
    List<Lecture> freeLecturesByRequestStatusAndSpeaker(@Param("status") RequestStatus status, @Param("speaker") User speaker);
}
