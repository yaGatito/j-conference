package com.epam.jconference.repository.impl;

import com.epam.jconference.model.Tag;
import com.epam.jconference.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TagRepositoryImpl implements TagRepository {

    private final List<Tag> tags = new ArrayList<>();

    @Override
    public Tag create(Tag tag) {
        tags.add(tag);
        int id = tags.indexOf(tag);
        tag.setId((long) id);
        tag.setTranslations(new HashMap<>());
        return tag;
    }

    @Override
    public Tag getById(Long id) {
        return tags.get(id.intValue());
    }

    @Override
    public Tag update(Tag tag) {
        Long id = tag.getId();
        Tag founded = tags.get(id.intValue());
        founded.setName(tag.getName());
        return founded;
    }

    @Override
    public Tag addTranslations(Long id, Map<String, String> translations) {
        Tag tag = tags.get(id.intValue());
        tag.getTranslations().putAll(translations);
        return tag;
    }

    @Override
    public List<Tag> findAll() {
        return tags;
    }
}
