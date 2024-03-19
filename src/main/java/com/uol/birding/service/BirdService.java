package com.uol.birding.service;

import com.uol.birding.dto.User;
import com.uol.birding.entity.Bird;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.BirdRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BirdService {

    private final BirdRepository birdRepository;
    private final UserService userService;

    public ResponseEntity getAllBirds(){
        try{
            return ResponseEntity
                    .ok(fetchAllBirds());
        }catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public Iterable<Bird> fetchAllBirds() {
        return birdRepository.findAll();
    }

    public Bird findBirdByCommonName(String commonName) {
        return birdRepository.findByCommonName(commonName).orElse(null);
    }

    public ResponseEntity addNewBird(String userName, Bird bird) {
        try {
            User user = userService.findUser(userName);
            if(UserType.USER == user.getUserType()) throw new Exception("User not authorized");
            if(birdRepository.findByCommonName(bird.getCommonName()).orElse(null) !=null) throw new Exception("Bird already exists!");
            birdRepository.save(bird);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Bird has been added.\"}");
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
