package com.epam.jconference.controller;

import com.epam.jconference.api.TagApi;
import com.epam.jconference.dto.TagDto;
import com.epam.jconference.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.DataTruncation;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TagController implements TagApi {

    private final TagService tagService;

    public TagDto create(@RequestBody @Valid TagDto tag) {
        return tagService.create(tag);
    }

    public TagDto getById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    public TagDto addTranslations(@PathVariable Long id, @RequestBody Map<String, String> translations) {
        return tagService.addTranslations(id, translations);
    }
}
