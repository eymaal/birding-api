package com.uol.birding.controller;

import com.uol.birding.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatController {

    final private StatService statService;

    @CrossOrigin
    @GetMapping("/v1/birdStats")
    public ResponseEntity getBirdStats() {
        return statService.getBirdStats();
    }

    @CrossOrigin
    @GetMapping("/v1/userStats")
    public ResponseEntity getUserStats() {
        return statService.getUserStats();
    }

    @CrossOrigin
    @GetMapping("/privilegedStats")
    public ResponseEntity getPrivilegedStats(String username) {
        return statService.getPrivilegedStats(username);
    }
}
