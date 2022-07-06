package com.epam.jconference.api;

import com.epam.jconference.dto.TagDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "Tag management API")
@RequestMapping("/api/v1/tags")
public interface TagApi {

    @ApiOperation("Create tag")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TagDto create(@RequestBody @Valid TagDto tag);

    @ApiOperation("Get tag by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TagDto getById(@PathVariable Long id);

    @ApiOperation("Get all tags")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TagDto> findAll();


    @ApiOperation("Add translations for tag")
    @PostMapping("/translations/{id}")
    @ResponseStatus(HttpStatus.OK)
    TagDto addTranslations(@PathVariable Long id, @RequestBody Map<String, String> translations);
}
