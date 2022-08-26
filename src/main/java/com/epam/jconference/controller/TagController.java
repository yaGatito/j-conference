package com.epam.jconference.controller;

import com.epam.jconference.api.TagApi;
import com.epam.jconference.dto.TagDto;
import com.epam.jconference.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TagController implements TagApi {

    private final TagService tagService;

    public TagDto create(TagDto tag) {
        return tagService.create(tag);
    }

    public TagDto getById(Long id) {
        return tagService.getById(id);
    }

    @Override
    public ResponseEntity<Void> deleteById(Long id) {
        return tagService.deleteById(id);
    }

    public List<TagDto> findAll() {
        return tagService.findAll();
    }
}
