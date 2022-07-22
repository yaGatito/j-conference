package com.epam.jconference.test.utils;

import com.epam.jconference.dto.TagDto;
import com.epam.jconference.model.Tag;

public class TestTagDataUtil {
    public static final Long TAG_ID1 = 1L;
    public static final Long TAG_ID2 = 2L;
    public static final String TAG_NAME1 = "java";
    public static final String TAG_NAME2 = "programming";

    public static Tag createTag1() {
        Tag tag = new Tag();
        tag.setId(TAG_ID1);
        tag.setName(TAG_NAME1);
        return tag;
    }

    public static Tag createTag2() {
        Tag tag = new Tag();
        tag.setId(TAG_ID2);
        tag.setName(TAG_NAME2);
        return tag;
    }

    public static TagDto createTag1Dto() {
        TagDto tagDto = new TagDto();
        tagDto.setName(TAG_NAME1);
        return tagDto;
    }

    public static TagDto createTag2Dto() {
        TagDto tagDto = new TagDto();
        tagDto.setName(TAG_NAME2);
        return tagDto;
    }
}
