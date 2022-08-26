package com.epam.jconference.api;

import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnRequest;
import com.epam.jconference.dto.validation.enums.EnumConstraint;
import com.epam.jconference.model.enums.LectureStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Api(tags = "Lectures management API")
@RequestMapping("/api/v1/lectures")
@Validated
public interface LectureApi {

    @ApiOperation("Create lectures. It is necessary to define lecture status.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/moder")
    LectureDto create(@RequestBody @Validated(OnCreate.class) LectureDto lectureDto);

    @ApiOperation("Get lecture by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/{id}")
    LectureDto findById(@PathVariable @Positive(message = "{id}{invalid}") Long id);


    @ApiOperation("Get all lectures by specified status")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/status/{status}")
    List<LectureDto> getLecturesForModer(
            @PathVariable @EnumConstraint(message = "{status}{not_exist}", value = LectureStatus.class) String status
    );

    @ApiOperation("Assign speaker to free lecture")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "speaker",
                    type = "requestParam",
                    required = true,
                    value = "ID of speaker that was served on this free lecture"),
            @ApiImplicitParam(
                    name = "free_lecture",
                    type = "requestParam",
                    required = true,
                    value = "ID of free lecture")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/free/assign")
    LectureDto assignSpeakerForFreeLecture(
            @RequestParam("speaker") @Positive(message = "{id}{invalid}") Long speakerId,
            @RequestParam("free_lecture") @Positive(message = "{id}{invalid}") Long lectureId
    );

    @ApiOperation("Reject request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/requests/reject/{id}")
    LectureDto rejectRequest(@PathVariable("id") @Positive(message = "{id}{invalid}") Long requestId);

    @ApiOperation("Accept request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/requests/accept/{id}")
    LectureDto acceptRequest(@PathVariable("id") @Positive(message = "{id}{invalid}") Long requestId);

    @ApiOperation("Requests history")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/history")
    List<LectureDto> moderHistory();

    @ApiOperation("Get all lectures secured to speaker. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/status/{status}")
    List<LectureDto> getLecturesForSpeaker(
            @PathVariable @EnumConstraint(message = "{status}{not_exist}", value = LectureStatus.class) String status
    );

    @ApiOperation("Create request for securing a free lecture. Must be /login")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/speaker/free/{lectureId}")
    LectureDto applyFreeLecture(@PathVariable @Positive(message = "{id}{invalid}") Long lectureId);

    @ApiOperation("Accept offer from moder. Must be /login")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/speaker/offers/accept/{lectureId}")
    LectureDto acceptOffer(@PathVariable @Positive(message = "{id}{invalid}") Long lectureId);

    @ApiOperation("Reject offer from moder. Must be /login")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/speaker/offers/reject/{lectureId}")
    LectureDto rejectOffer(@PathVariable @Positive(message = "{id}{invalid}") Long lectureId);

    @ApiOperation("Create request for giving a lecture in defined event. Must be /login")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/speaker/requests")
    LectureDto addRequest(@RequestBody @Validated(OnRequest.class) LectureDto lectureDto);

    @ApiOperation("Get speaker history. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/history")
    List<LectureDto> speakerHistory();
}
