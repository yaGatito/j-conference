package com.epam.jconference.api;

import com.epam.jconference.dto.LectureDto;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Lectures management API")
@RequestMapping("/api/v1/lectures")
public interface LectureApi {

    @ApiOperation("Create lectures. It is necessary to define lecture status.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    LectureDto create(@RequestBody @Valid LectureDto lectureDto);

    @ApiOperation("Get lecture by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    LectureDto findById(@PathVariable Long id);

    @ApiOperation("Assign speaker to free lecture")
    @ApiImplicitParams({@ApiImplicitParam(name = "speaker", type = "requestParam", required = true, value = "ID of speaker that was served on this free lecture"), @ApiImplicitParam(name = "free-lecture", type = "requestParam", required = true, value = "ID of free lecture")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/assign")
    LectureDto assignSpeakerForFreeLecture(@RequestParam("speaker") Long speakerId, @RequestParam("free-lecture") Long lectureId);

    @ApiOperation("Reject request. User that want reject some request should be logged in")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/request/reject")
    LectureDto rejectRequest(Long requestId);

    @ApiOperation("Accept request. User that want accept some request should be logged in")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/request/accept")
    LectureDto acceptRequest(Long requestId);

    @ApiOperation("History of requests. Accepted and pending")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/moder/history")
    List<LectureDto> moderHistory();

    //MIXED ACCESS
    @ApiOperation("Get all available free lectures")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/free")
    List<LectureDto> getFreeLectures();

    //SPEAKER SECTION
    @ApiOperation("Get all secured lectures by speaker that should be logged in")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/secured")
    List<LectureDto> getSecuredLectures();

    @ApiOperation("Get all offers that were sent by moder")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/offers")
    List<LectureDto> getOffers();

    @ApiOperation("Accept offer from moder")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/offers/accept/{lectureId}")
    LectureDto acceptOffer(@PathVariable Long lectureId);

    @ApiOperation("Accept offer from moder")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/offers/decline/{lectureId}")
    LectureDto declineOffer(@PathVariable Long lectureId);

    @ApiOperation("Get requests that were made by logged speaker")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/requests")
    List<LectureDto> getRequests();

    @ApiOperation("Create request for giving a lecture in defined event")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/requests")
    LectureDto addRequest(@RequestBody @Valid LectureDto lectureDto);

    @ApiOperation("Create request for giving a free lecture in defined event")
    @ApiImplicitParam(name = "lectureId", type = "path", required = true, value = "Id of lecture")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/free/{lectureId}")
    LectureDto applyFreeLecture(@PathVariable Long lectureId);

    @ApiOperation("Get speaker history")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/speaker/history")
    List<LectureDto> speakerHistory();
}
