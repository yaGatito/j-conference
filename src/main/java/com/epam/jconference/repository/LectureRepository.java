package com.epam.jconference.repository;

import com.epam.jconference.model.Event;
import com.epam.jconference.model.Lecture;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.LectureStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    Integer existsFreeLecture(@Param("event") Event event, @Param("topic") String topic);

    List<Lecture> findAllByStatus(LectureStatus lectureStatus);

    List<Lecture> findAllByStatusAndSpeaker(LectureStatus status, User speaker);
}
