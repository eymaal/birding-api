package com.uol.birding.service;
import com.uol.birding.dto.User;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.PostRepository;
import com.uol.birding.repository.SightingRepository;
import com.uol.birding.repository.UnmoderatedSightingRepository;
import com.uol.birding.repository.UserRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SightingRepository sightingRepository;
    private final UnmoderatedSightingRepository unmoderatedSightingRepository;
    private final PostRepository postRepository;

    public User findUser(String id) throws Exception {
        com.uol.birding.entity.User user = userRepository.findByEmail(id).orElseThrow(() -> new Exception("User not found"));
        User userDto = User
                .builder()
                .name(user.getFirstName() + " " + user.getLastName())
                .username(user.getEmail())
                .userType(user.getUserType())
                .build();
        return userDto;

    }

    public ResponseEntity getUsersByType(String userName, UserType userType) {
        try {
            User user = findUser(userName);
            if( UserType.USER == user.getUserType()) throw new Exception("User not authorized");
            List<com.uol.birding.entity.User> users = userRepository.findAllByUserType(userType);
            List<User> userList = new ArrayList<>();
            users.stream().forEach((u) -> {
                User userDto = User
                        .builder()
                        .name(u.getFirstName() + " " + u.getLastName())
                        .userType(u.getUserType())
                        .username(u.getEmail())
                        .locked(u.isLocked())
                        .build();
                userList.add(userDto);
            });
            return ResponseEntity
                    .ok()
                    .body(userList);
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity updateUsers(String userName, List<User> users) {
        try {
            User user = findUser(userName);
            if(UserType.USER == user.getUserType()) throw new Exception("User not authorized");
            users.stream().forEach((u) -> {
                userRepository.updateUserLock(u.getUsername(), u.getLocked());
            });
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Changes have been saved.\"}");
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity deleteUsers(String userName, List<User> users) {
        try {
            User user = findUser(userName);
            if(UserType.ADMIN != user.getUserType()) throw new Exception("User not authorized");
            users.forEach(u -> {
                com.uol.birding.entity.User userDao = userRepository.findByEmail(u.getUsername()).orElse(null);
                if(userDao != null) userRepository.delete(userDao);
            });
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Changes have been saved.\"}");
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity deleteProfile(String userName) {
        try {
            User user = findUser(userName);
            if(user==null) {
                throw new Exception("User does not exist!");
            }
            sightingRepository.deleteAllByObserverId(userName);
            unmoderatedSightingRepository.deleteAllByObserverId(userName);
            postRepository.deleteAllByAuthor(userName);
            com.uol.birding.entity.User u = userRepository.findByEmail(user.getUsername()).orElse(null);
            if(u!=null) userRepository.delete(u);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
