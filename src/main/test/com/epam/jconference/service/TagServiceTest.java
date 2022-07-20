package com.epam.jconference.service;

import com.epam.jconference.dto.TagDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.model.Tag;
import com.epam.jconference.repository.TagRepository;
import com.epam.jconference.service.impl.TagServiceImpl;
import com.epam.jconference.test.utils.TestTagDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static com.epam.jconference.test.utils.TestTagDataUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagJpaRepository;
    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void createTagTest() {
        Tag tag1 = TestTagDataUtil.createTag1();
        when(tagJpaRepository.existsByName(tag1.getName())).thenReturn(false);
        when(tagJpaRepository.save(tag1)).thenReturn(tag1);
        TagDto tag1Dto = createTag1Dto();

        TagDto tagDto = tagService.create(tag1Dto);
        assertEquals(tag1Dto.getName(), tagDto.getName());
    }

    @Test
    void createAlreadyExistedTagTest() {
        TagDto tag1Dto = createTag1Dto();
        when(tagJpaRepository.existsByName(tag1Dto.getName())).thenReturn(true);

        assertThrows(InvalidOperationException.class, () -> tagService.create(tag1Dto));
    }

    @Test
    void getByIdTagTest() {
        Tag tag1 = TestTagDataUtil.createTag1();
        when(tagJpaRepository.existsById(any())).thenReturn(true);
        when(tagJpaRepository.getById(any())).thenReturn(tag1);

        TagDto tagDto = tagService.getById(TAG_ID1);
        assertThat(tagDto, allOf(
                hasProperty("id", equalTo(tag1.getId())),
                hasProperty("name", equalTo(tag1.getName()))));
    }

    @Test
    void getByIdNotExistedTagTest() {
        Tag tag1 = TestTagDataUtil.createTag1();
        when(tagJpaRepository.existsById(any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> tagService.getById(TAG_ID1));
    }

    @Test
    void findAllTags() {
        Tag tag1 = createTag1();
        Tag tag2 = createTag2();
        when(tagJpaRepository.findAll()).thenReturn(List.of(tag1, tag2));

        List<TagDto> all = tagService.findAll();
        assertEquals(all.size(), all.stream()
                                    .filter(t -> Objects.nonNull(t.getName()) && Objects.nonNull(t.getId()))
                                    .count());
    }
}
