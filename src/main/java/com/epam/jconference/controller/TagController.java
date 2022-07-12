package com.epam.jconference.controller;

import com.epam.jconference.service.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class TagController {
    @PostMapping("/tags")
    public Tag create(Tag tag) {
        return null;
    }

    @GetMapping("/tags")
    public List<Tag> findAll() {
        return null;
    }

    @GetMapping("/tags/{id}")
    public List<Tag> findById(@PathVariable Long id) {
        return null;
    }

    @PatchMapping("/tags")
    public Tag update(Tag tag) {
        return null;
    }

    @DeleteMapping("/tags")
    public Tag delete(Tag tag) {
        return null;
    }

}
