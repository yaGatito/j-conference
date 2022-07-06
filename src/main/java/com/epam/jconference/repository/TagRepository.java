package com.epam.jconference.repository;

import com.epam.jconference.model.Tag;

import java.util.List;
import java.util.Map;

public interface TagRepository {
    Tag create(Tag tag);

    Tag getById(Long id);

    Tag update(Tag tag);

    Tag addTranslations(Long id, Map<String, String> translations);

    List<Tag> findAll();
}
