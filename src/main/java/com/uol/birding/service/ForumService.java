package com.uol.birding.service;

import com.uol.birding.dto.User;
import com.uol.birding.entity.Post;
import com.uol.birding.repository.PostRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final PostRepository postRepository;
    private final UserService userService;

    public ResponseEntity getAllPosts(String userName) {
        try {
            User user = userService.findUser(userName);
            if(user==null) throw new Exception("User not authorized");
            return ResponseEntity
                    .ok(postRepository.findByOrderByDateTimeDesc());
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity makeNewPost(String userName, Post post) {
        try {
            User user = userService.findUser(userName);
            if(user==null) throw new Exception("User not authorized");
            post.setDateTime(LocalDateTime.now());
            post.setTags(Arrays.stream(post.getTags().split("\\s+"))
                    .map(word -> "#" + word)
                    .collect(Collectors.joining(" ")));
            postRepository.save(post);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Post has been saved.\"}");
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
