package com.epam.jconference.controller;

import com.epam.jconference.config.TestConfig;
import com.epam.jconference.dto.TagDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.model.enums.ErrorType;
import com.epam.jconference.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.epam.jconference.test.utils.TestTagDataUtil.createTag1Dto;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
@Import(TestConfig.class)
public class TagControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final String baseUrl = "/api/v1/tags";
    @MockBean
    private TagService tagService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTagTest() throws Exception {
        TagDto tag1Dto = createTag1Dto();
        TagDto persisted = createTag1Dto();
        persisted.setId(1L);

        String body = objectMapper.writeValueAsString(tag1Dto);
        when(tagService.create(tag1Dto)).thenReturn(persisted);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().isCreated(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$.name").value(tag1Dto.getName())
               );
    }

    @Test
    void createInvalidTagTest() throws Exception {
        TagDto tag1Dto = createTag1Dto();
        tag1Dto.setName("!ASDlasd+@#@");
        String body = objectMapper.writeValueAsString(tag1Dto);

        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(body))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$[0].errorType").value(ErrorType.VALIDATION_ERROR_TYPE.name())
               );
    }

    @Test
    void getTagByIdTest() throws Exception {
        TagDto persisted = createTag1Dto();
        persisted.setId(1L);

        when(tagService.getById(1L)).thenReturn(persisted);

        mockMvc.perform(get(baseUrl + "/" + 1L))
               .andDo(print())
               .andExpectAll(
                       status().isOk(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$.id").isNumber(),
                       jsonPath("$.name").value(persisted.getName())
               );
    }

    @Test
    void getNotExistedTagByIdTest() throws Exception {
        TagDto persisted = createTag1Dto();
        persisted.setId(1L);

        when(tagService.getById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get(baseUrl + "/" + 1L))
               .andDo(print())
               .andExpectAll(
                       status().is4xxClientError(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$.errorType").value(ErrorType.NOT_FOUND_ERROR_TYPE.name())
               );
    }

    @Test
    void getAllTagsTest() throws Exception {
        mockMvc.perform(get(baseUrl))
               .andDo(print())
               .andExpectAll(
                       status().isOk(),
                       content().contentType(MediaType.APPLICATION_JSON),
                       jsonPath("$").isArray()
               );
    }
}
