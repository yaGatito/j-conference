package com.epam.jconference.controller;

import com.epam.jconference.controller.dto.LectureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class LectureController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/lectures/create")
    public LectureDto create(@RequestBody LectureDto lectureDto) {
        return null;
    }

    @GetMapping("/lectures/{id}")
    public LectureDto findById(@PathVariable Long id) {
        return null;
    }

    @PatchMapping("/lectures/{id}")
    public LectureDto update(@RequestBody LectureDto lectureDto) {
        return null;
    }

    @DeleteMapping("/lectures/{id}")
    public LectureDto delete(@PathVariable Long id) {
        return null;
    }
}
