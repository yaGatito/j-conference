package com.epam.jconference.controller;

import com.epam.jconference.controller.dto.LectureDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ModerController {

    @GetMapping("/lectures/moder/requests")
    public LectureDto requestsModer(){
        return null;
    }

    @GetMapping("/lectures/moder/secured")
    public LectureDto securedModer(){
        return null;
    }

    @GetMapping("/lectures/moder/free")
    public LectureDto freeLectures(){
        return null;
    }

    @GetMapping("/lectures/moder/history")
    public LectureDto history(){
        return null;
    }

}
