package com.uol.birding.controller;

import com.uol.birding.auth.AuthenticationRequest;
import com.uol.birding.auth.AuthenticationService;
import com.uol.birding.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/")
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping("v1/register")
    public ResponseEntity register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @CrossOrigin
    @PostMapping("v1/authenticate")
    public ResponseEntity authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @CrossOrigin
    @PostMapping("/addModerator")
    public ResponseEntity addModerator(@RequestParam String userName, @RequestBody RegisterRequest request) {
        return authenticationService.registerMod(request, userName);
    }
}
