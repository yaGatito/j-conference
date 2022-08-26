package com.epam.jconference.service;

import com.epam.jconference.dto.TagDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TagService {
    TagDto create(TagDto tag);

    TagDto getById(Long id);

    ResponseEntity<Void> deleteById(Long id);

    List<TagDto> findAll();
}
