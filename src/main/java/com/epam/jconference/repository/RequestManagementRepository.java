package com.epam.jconference.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestManagementRepository {
    void assignSpeakerOnFreeLecture(Long speaker, Long lecture);

    void applyOnFreeLecture(Long speaker, Long lecture);

    List<Long> appliedFreeLectures(Long speakerId);

    boolean existsApplicationOnFreeLecture(Long speaker, Long lecture);
}
