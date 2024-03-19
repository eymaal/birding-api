package com.uol.birding.controller;

import com.uol.birding.dto.User;
import com.uol.birding.entity.Settings;
import com.uol.birding.enums.UserType;
import com.uol.birding.service.SettingService;
import com.uol.birding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;

    @CrossOrigin
    @GetMapping("/users")
    public ResponseEntity getUsers(@RequestParam String userName) {
        return userService.getUsersByType(userName, UserType.USER);
    }

    @CrossOrigin
    @GetMapping("/moderators")
    public ResponseEntity getModerators(@RequestParam String userName) {
        return userService.getUsersByType(userName, UserType.MODERATOR);
    }

    @CrossOrigin
    @GetMapping("/settings")
    public ResponseEntity getSettings(@RequestParam String userName) {
        return settingService.getSettings(userName);
    }

    @CrossOrigin
    @PostMapping("/updateSettings")
    public ResponseEntity updateSettings(@RequestParam String userName, @RequestBody List<Settings> settings) {
        return settingService.updateSettings(userName, settings);
    }

    @CrossOrigin
    @PostMapping("/updateUsers")
    public ResponseEntity updateUsers(@RequestParam String userName, @RequestBody List<User> users) {
        return userService.updateUsers(userName, users);
    }

    @CrossOrigin
    @DeleteMapping("/deleteUsers")
    public ResponseEntity deleteUsers(@RequestParam String userName, @RequestBody List<User> users) {
        return userService.deleteUsers(userName, users);
    }

    @CrossOrigin
    @DeleteMapping("/deleteProfile")
    public ResponseEntity deleteProfile(@RequestParam String userName) {
        return userService.deleteProfile(userName);
    }

}
