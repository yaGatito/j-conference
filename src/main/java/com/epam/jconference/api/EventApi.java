package com.epam.jconference.api;

import com.epam.jconference.dto.EventDto;
import com.epam.jconference.dto.EventPagingSortingFilterDto;
import com.epam.jconference.dto.group.OnCreate;
import com.epam.jconference.dto.group.OnUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Api(tags = "Event management API")
@RequestMapping(value = "/api/v1/events")
@Validated
public interface EventApi {
    @ApiOperation("Create event")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    EventDto create(@RequestBody @Validated(OnCreate.class) EventDto eventDto);

    @ApiOperation("Get all events by filtering entity")
    @ApiImplicitParam(
            name = "eventFilter",
            type = "requestBody",
            required = false,
            value = "Has default values for sorting and filtering"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    List<EventDto> getAll(@RequestBody @Valid EventPagingSortingFilterDto eventFilter);

    @ApiOperation("Get by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    EventDto getById(@PathVariable @Positive(message = "{id}{invalid}") Long id);

    @ApiOperation("Update event properties")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping
    EventDto update(@RequestBody @Validated(OnUpdate.class) EventDto eventDto);

    @ApiOperation("Delete by id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteById(@PathVariable @Positive(message = "{id}{invalid}") Long id);

    @ApiOperation("Show all participation of logged user")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/participation")
    List<EventDto> participation();

    @ApiOperation("Join the event. User must be logged in.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/join/{eventId}")
    ResponseEntity<Void> join(@PathVariable @Positive(message = "{id}{invalid}") Long eventId);

    @ApiOperation("Leave the event. User must be logged in.")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/leave/{eventId}")
    ResponseEntity<Void> leave(@PathVariable @Positive(message = "{id}{invalid}") Long eventId);
}
