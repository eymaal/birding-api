package com.uol.birding.controller;

import com.uol.birding.entity.Bird;
import com.uol.birding.service.BirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BirdController {

    @Autowired
    private BirdService birdService;

    @GetMapping("/v1/birds")
    @CrossOrigin
    public ResponseEntity getAllBirds() {
        return birdService.getAllBirds();
    }

    @CrossOrigin
    @PostMapping("/newBird")
    public ResponseEntity addNewBird(@RequestParam String userName, @RequestBody Bird bird) {
        return birdService.addNewBird(userName, bird);
    }
}
