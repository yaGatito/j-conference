package com.epam.jconference.controller;

import com.epam.jconference.api.TagApi;
import com.epam.jconference.dto.TagDto;
import com.epam.jconference.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    public TagDto addTranslations(Long id, Map<String, String> translations) {
        return tagService.addTranslations(id, translations);
    }

    public List<TagDto> findAllById(List<Long> ids) {
        return tagService.findAllByID(ids);
    }
}
