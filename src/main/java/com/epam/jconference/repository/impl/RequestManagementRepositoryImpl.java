package com.epam.jconference.repository.impl;

import com.epam.jconference.repository.RequestManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RequestManagementRepositoryImpl implements RequestManagementRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void assignSpeakerOnFreeLecture(Long speaker, Long lecture) {
        jdbcTemplate.update("update free_requests set status = 0 where free_lecture_id = ? and speaker_id != ?;", lecture, speaker);
        jdbcTemplate.update("update free_requests set status = 2 where free_lecture_id = ? and speaker_id = ?;", lecture, speaker);
    }

    public void applyOnFreeLecture(Long speaker, Long lecture) {
        jdbcTemplate.update("insert into free_requests (status, free_lecture_id, speaker_id) VALUES (1, ?, ?);", lecture, speaker);
    }

    public List<Long> appliedFreeLectures(Long speakerId) {
        return jdbcTemplate.query("select free_lecture_id from free_requests where speaker_id = " + speakerId + ";",
                rs -> {
                    List<Long> ids = new ArrayList<>();
                    while (rs.next()) {
                        ids.add((long)rs.getInt(1));
                    }
                    return ids;
                });
    }
}
