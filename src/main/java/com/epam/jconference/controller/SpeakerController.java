package com.epam.jconference.controller;

import com.epam.jconference.controller.dto.LectureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SpeakerController {

    @GetMapping("/lectures/speaker/offers")
    public LectureDto offeredLectures(){
        return null;
    }

    @GetMapping("/lectures/speaker/requests")
    public LectureDto requestedLectures(){
        return null;
    }

    @GetMapping("/lectures/speaker/secured")
    public LectureDto securedLectures(){
        return null;
    }

    @GetMapping("/lectures/speaker/free")
    public LectureDto freeLectures(){
        return null;
    }

    @GetMapping("/lectures/speaker/history")
    public LectureDto history(){
        return null;
    }
}
