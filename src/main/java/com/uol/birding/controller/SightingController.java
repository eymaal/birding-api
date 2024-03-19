package com.uol.birding.controller;

import com.uol.birding.dto.SightingRequest;
import com.uol.birding.service.SightingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SightingController {

    @Autowired
    private SightingService sightingService;

    @GetMapping("/v1/sightings")
    @CrossOrigin
    public ResponseEntity getAllSightings(){
        return sightingService.getAllSightings();
    }

    @CrossOrigin
    @PostMapping("/newSighting")
    public ResponseEntity addNewSighting(@RequestBody SightingRequest sightingRequest) {
        return sightingService.addNewSighting(sightingRequest);
    }

    @CrossOrigin
    @GetMapping("/userSightings")
    public ResponseEntity getUserSightings(@RequestParam String userName) {
        return sightingService.getUserSightings(userName);
    }

    @CrossOrigin
    @GetMapping("/unmoderratedSightings")
    public ResponseEntity getUnmoderatedSightings(@RequestParam String userName) {
        return sightingService.getUnmoderatedSightings(userName);
    }

    @CrossOrigin
    @PostMapping("/approveSightings")
    public ResponseEntity approveSightings(@RequestParam String userName, @RequestBody List<Integer> unmoderatedIds) {
        return sightingService.approveSightings(userName, unmoderatedIds);
    }

    @CrossOrigin
    @DeleteMapping("/discardSightings")
    public ResponseEntity discardSightings(@RequestParam String userName, @RequestBody List<Integer> unmoderatedIds) {
        return sightingService.discardSightings(userName, unmoderatedIds);
    }
}
