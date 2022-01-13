package com.controller;

import com.context.Friend;
import com.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService service;

    public FriendController(FriendService service) {
        this.service = service;
    }

    @Operation(summary = "Search friends", operationId = "getFriends")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found friends",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object[].class))}
            ),
            @ApiResponse(responseCode = "204", description = "No friends found")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Friend>> getFriends() {
        List<Friend> friends = service.getFriends();

        if (friends.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(friends);
    }

    @Operation(summary = "Get an friend", operationId = "getFriend")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found friend",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = java.lang.Object.class))}
            ),
            @ApiResponse(responseCode = "404", description = "No friend found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Friend> getFriend(@PathVariable Long id) {
        Optional<Friend> friend = service.getFriendById(id);

        if (friend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(friend.get());
    }

    @Operation(summary = "Create an friend", operationId = "addFriend")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Friend was created",
                    headers = {@Header(name = "location", schema = @Schema(type = "String"))}
            ),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping(value = "", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addFriend(@RequestBody Friend friend) {
        service.saveFriend(friend);
        URI uri = WebMvcLinkBuilder.linkTo(this.getClass()).slash(friend.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Update an friend", operationId = "updateFriend")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Friend was updated"),
            @ApiResponse(responseCode = "404", description = "Friend not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateFriend(@PathVariable Long id, @RequestBody Friend friend) {
        Optional<Friend> existingFriend = service.getFriendById(id);

        if (existingFriend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        existingFriend.get().updateFriend(friend);
        service.saveFriend(existingFriend.get());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete an friend", operationId = "deleteFriend")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Friend was deleted"),
            @ApiResponse(responseCode = "404", description = "Friend not found"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id) {
        Optional<Friend> existingFriend = service.getFriendById(id);

        if (existingFriend.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        service.deleteFriend(existingFriend.get());
        return ResponseEntity.noContent().build();
    }
}
