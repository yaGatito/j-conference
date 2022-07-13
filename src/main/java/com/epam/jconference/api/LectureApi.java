package com.epam.jconference.api;

import com.epam.jconference.dto.LectureDto;
import com.epam.jconference.dto.group.OnRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Lectures management API")
@RequestMapping("/api/v1/lectures")
public interface LectureApi {

    @ApiOperation("Create lectures. It is necessary to define lecture status.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/moder")
    LectureDto create(@RequestBody @Valid LectureDto lectureDto);

    @ApiOperation("Get lecture by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/{id}")
    LectureDto findById(@PathVariable Long id);


    @ApiOperation("Get all secured lectures")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/secured")
    List<LectureDto> getSecuredLecturesForModer();

    @ApiOperation("Get all free lectures")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/free")
    List<LectureDto> getFreeLecturesForModer();

    @ApiOperation("Assign speaker to free lecture")
    @ApiImplicitParams({@ApiImplicitParam(name = "speaker", type = "requestParam", required = true, value = "ID of speaker that was served on this free lecture"), @ApiImplicitParam(name = "free-lecture", type = "requestParam", required = true, value = "ID of free lecture")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/free/assign")
    LectureDto assignSpeakerForFreeLecture(@RequestParam("speaker") Long speakerId, @RequestParam("free-lecture") Long lectureId);

    @ApiOperation("Get all requests")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/requests")
    List<LectureDto> getRequestsForModer();

    @ApiOperation("Reject request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/requests/reject/{id}")
    LectureDto rejectRequest(@PathVariable("id") Long requestId);

    @ApiOperation("Accept request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/requests/accept/{id}")
    LectureDto acceptRequest(@PathVariable("id") Long requestId);

    @ApiOperation("Get all offers")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/offers")
    List<LectureDto> getOffersForModer();

    @ApiOperation("Requests history")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/moder/history")
    List<LectureDto> moderHistory();

    @ApiOperation("Get all lectures secured to speaker. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/secured")
    List<LectureDto> getSecuredLectures();

    @ApiOperation("Get all available free lectures to speaker. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/free")
    List<LectureDto> getFreeLectures();

    @ApiOperation("Create request for securing a free lecture. Must be /login")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/speaker/free/{lectureId}")
    LectureDto applyFreeLecture(@PathVariable Long lectureId);

    @ApiOperation("Get all offers that were sent to current speaker. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/offers")
    List<LectureDto> getOffers();

    @ApiOperation("Accept offer from moder. Must be /login")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/speaker/offers/accept/{lectureId}")
    LectureDto acceptOffer(@PathVariable Long lectureId);

    @ApiOperation("Reject offer from moder. Must be /login")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/speaker/offers/reject/{lectureId}")
    LectureDto rejectOffer(@PathVariable Long lectureId);

    @ApiOperation("Get requests that were made by logged speaker. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/requests")
    List<LectureDto> getRequests();

    @ApiOperation("Create request for giving a lecture in defined event. Must be /login")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/speaker/requests")
    LectureDto addRequest(@RequestBody @Validated(OnRequest.class) LectureDto lectureDto);

    @ApiOperation("Get speaker history. Must be /login")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/history")
    List<LectureDto> speakerHistory();
}
