package com.epam.jconference.api;

import com.epam.jconference.dto.TagDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Api(tags = "Tag management API")
@RequestMapping("/api/v1/tags")
@Validated
public interface TagApi {

    @ApiOperation("Create tag")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    TagDto create(@RequestBody @Valid TagDto tag);

    @ApiOperation("Get tag by id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TagDto getById(@PathVariable @Positive(message = "{id}{invalid}") Long id);

    @ApiOperation("Delete tag by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Void> deleteById(@PathVariable @Positive(message = "{id}{invalid}") Long id);

    @ApiOperation("Get all tags")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TagDto> findAll();
}
