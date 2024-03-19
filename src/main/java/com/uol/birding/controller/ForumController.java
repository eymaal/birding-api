package com.uol.birding.controller;

import com.uol.birding.entity.Post;
import com.uol.birding.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @GetMapping("/posts")
    @CrossOrigin
    public ResponseEntity getAllPosts(@RequestParam String userName) {
        return forumService.getAllPosts(userName);
    }

    @PostMapping("/newPost")
    @CrossOrigin
    public ResponseEntity makeNewPost(@RequestParam String userName, @RequestBody Post post) {
        return forumService.makeNewPost(userName, post);
    }
}
