package com.epam.jconference.service.impl;

import com.epam.jconference.dto.TagDto;
import com.epam.jconference.mapper.TagMapper;
import com.epam.jconference.repository.TagRepository;
import com.epam.jconference.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagJpaRepository;

    private final TagMapper mapper = TagMapper.INSTANCE;

    @Override
    public TagDto create(TagDto tag) {
        return mapper.mapToDto(tagJpaRepository.save(mapper.mapToEntity(tag)));
    }

    @Override
    public TagDto getById(Long id) {
        return mapper.mapToDto(tagJpaRepository.getById(id));
    }

    @Override
    public TagDto update(TagDto tag) {
        if (tagJpaRepository.existsById(tag.getId())) {
            mapper.mapToDto(tagJpaRepository.save(mapper.mapToEntity(tag)));
        }
        return tag;
    }

    @Override
    public TagDto addTranslations(Long id, Map<String, String> translations) {
        return null;
    }

    @Override
    public List<TagDto> findAll() {
        return tagJpaRepository.findAll().stream().map(mapper::mapToDto).collect(Collectors.toList());
    }

    public List<TagDto> findAllByID(List<Long> ids){
        return tagJpaRepository.findAllById( () -> ids.stream().iterator()).stream().map(mapper::mapToDto).collect(Collectors.toList());
    }
}
