package com.epam.jconference.service.impl;

import com.epam.jconference.dto.TagDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.mapper.TagMapper;
import com.epam.jconference.repository.TagRepository;
import com.epam.jconference.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagJpaRepository;

    private final TagMapper mapper = TagMapper.INSTANCE;

    @Override
    public TagDto create(TagDto tag) {
        if (tagJpaRepository.existsByName(tag.getName())) {
            throw new InvalidOperationException("Tag already exist");
        }
        return mapper.mapToDto(tagJpaRepository.save(mapper.mapToEntity(tag)));
    }

    @Override
    public TagDto getById(Long id) {
        if (!tagJpaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag doesn't exist");
        }
        return mapper.mapToDto(tagJpaRepository.getById(id));
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteById(Long id) {
        if (!tagJpaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag doesn't exist");
        }
        tagJpaRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<TagDto> findAll() {
        return tagJpaRepository.findAll().stream().map(mapper::mapToDto).collect(Collectors.toList());
    }
}
