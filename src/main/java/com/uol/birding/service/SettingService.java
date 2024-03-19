package com.uol.birding.service;

import com.uol.birding.dto.User;
import com.uol.birding.entity.Settings;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.SettingsRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingsRepository settingsRepository;
    private final UserService userService;
    public ResponseEntity getSettings(String userName) {
        try{
            User user = userService.findUser(userName);
            if(user.getUserType()!= UserType.ADMIN) throw new Exception("User is not an Admin");
            List<Settings> settings = IterableUtils.toList(settingsRepository.findAll());
            return ResponseEntity
                .ok().body(settings);
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity updateSettings(String userName, List<Settings> settings) {
        try{
            User user = userService.findUser(userName);
            if(user.getUserType()!= UserType.ADMIN) throw new Exception("User is not an Admin");
            settingsRepository.saveAll(settings);
            return ResponseEntity
                    .status(HttpStatus.OK).build();
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
