package com.epam.jconference.service.impl;

import com.epam.jconference.dto.TagDto;
import com.epam.jconference.mapper.TagMapper;
import com.epam.jconference.repository.TagRepository;
import com.epam.jconference.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public TagDto create(TagDto tag) {
        return TagMapper.INSTANCE.mapToDto(tagRepository.create(TagMapper.INSTANCE.mapToEntity(tag)));
    }

    @Override
    public TagDto getById(Long id) {
        return TagMapper.INSTANCE.mapToDto(tagRepository.getById(id));
    }

    @Override
    public TagDto update(TagDto tag) {
        return TagMapper.INSTANCE.mapToDto(tagRepository.update(TagMapper.INSTANCE.mapToEntity(tag)));
    }

    @Override
    public TagDto addTranslations(Long id, Map<String, String> translations) {
        return TagMapper.INSTANCE.mapToDto(tagRepository.addTranslations(id, translations));
    }

    @Override
    public List<TagDto> findAll() {
        return tagRepository.findAll().stream().map(TagMapper.INSTANCE::mapToDto).collect(Collectors.toList());
    }
}
