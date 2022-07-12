package com.epam.jconference.repository;

import java.util.List;

public interface RequestManagementRepository {
    void assignSpeakerOnFreeLecture(Long speaker, Long lecture);

    void applyOnFreeLecture(Long speaker, Long lecture);

    List<Long> appliedFreeLectures(Long speakerId);
}
