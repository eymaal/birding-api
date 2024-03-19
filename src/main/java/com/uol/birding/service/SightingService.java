package com.uol.birding.service;

import com.uol.birding.dto.SightingRequest;
import com.uol.birding.dto.User;
import com.uol.birding.entity.Bird;
import com.uol.birding.entity.Sighting;
import com.uol.birding.entity.UnmoderatedSighting;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.SettingsRepository;
import com.uol.birding.repository.SightingRepository;
import com.uol.birding.repository.UnmoderatedSightingRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SightingService {

    @Autowired
    private SightingRepository sightingRepository;
    private final UnmoderatedSightingRepository unmoderatedSightingRepository;
    private final SettingsRepository settingsRepository;
    private final BirdService birdService;
    private final UserService userService;

    public ResponseEntity getAllSightings(){
        try{
             Iterable<Sighting> sightings = sightingRepository.findAll();
             var sightingList = prepareSightingDtoList(sightings.iterator());
             return ResponseEntity.ok(sightingList);
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    private List<com.uol.birding.dto.Sighting> prepareSightingDtoList(Iterator<Sighting> sightings) throws Exception {
        List<Bird> birds = IterableUtils.toList(birdService.fetchAllBirds());
        List<com.uol.birding.dto.Sighting> sightingList = new ArrayList<>();
        while (sightings.hasNext()) {
            Sighting sighting = sightings.next();
            Bird bird = birds
                    .stream()
                    .filter(b -> b.getId().equals(sighting.getBirdId()))
                    .findFirst().orElseThrow(() -> new Exception("Bird not found!"));
            com.uol.birding.dto.Bird birdDto = com.uol.birding.dto.Bird
                    .builder()
                    .commonName(bird.getCommonName())
                    .scientificName(bird.getScientificName())
                    .classification(bird.getClassification())
                    .taxonomicGrouping(bird.getTaxonomicGrouping())
                    .family(bird.getFamily())
                    .imageRef(bird.getImageRef())
                    .build();
            User user = userService.findUser(sighting.getObserverId());
            com.uol.birding.dto.Sighting sightingDto = com.uol.birding.dto.Sighting
                    .builder()
                    .id(sighting.getId())
                    .creator(user)
                    .bird(birdDto)
                    .latitude(sighting.getLatitude())
                    .longitude(sighting.getLongitude())
                    .quantity(sighting.getQuantity())
                    .date(sighting.getDate())
                    .imageRef(sighting.getImageRef())
                    .build();
            if (UserType.USER != user.getUserType()) sightingDto.setApprover(sighting.getApproverId());
            sightingList.add(sightingDto);
        }
        return sightingList;
    }

    public ResponseEntity addNewSighting(SightingRequest sightingRequest){
        try{
            User user = userService.findUser(sightingRequest.getUserEmail());
            if(user==null) throw new Exception("User does not exist!");
            if(settingsRepository.findValueByName("REQUIRE_MODERATOR_APPROVAL")) {
                UnmoderatedSighting sighting = UnmoderatedSighting
                        .builder()
                        .observerId(user.getUsername())
                        .birdId(birdService.findBirdByCommonName(sightingRequest.getBirdSpecies()).getId())
                        .latitude(sightingRequest.getLatitude())
                        .longitude(sightingRequest.getLongitude())
                        .quantity(sightingRequest.getQuantity())
                        .date(sightingRequest.getDate())
                        .imageRef(sightingRequest.getImageRef())
                        .build();
                unmoderatedSightingRepository.save(sighting);
            } else {
                Sighting sighting = Sighting
                        .builder()
                        .observerId(user.getUsername())
                        .birdId(birdService.findBirdByCommonName(sightingRequest.getBirdSpecies()).getId())
                        .latitude(sightingRequest.getLatitude())
                        .longitude(sightingRequest.getLongitude())
                        .quantity(sightingRequest.getQuantity())
                        .date(sightingRequest.getDate())
                        .imageRef(sightingRequest.getImageRef())
                        .build();
                sightingRepository.save(sighting);
            }
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Sightings has been saved\"}");
        }catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity getUserSightings(String userName) {
        try{
            User user = userService.findUser(userName);
            if(user==null) throw new Exception("User does not exist!");
            Iterator<Sighting> sightings = sightingRepository.findAllByObserverId(userName).iterator();
            var sightingList = prepareSightingDtoList(sightings);
            return ResponseEntity.ok(sightingList);
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
    public ResponseEntity getUnmoderatedSightings(String userName) {
        try {
            User user = userService.findUser(userName);
            if(user==null) throw new Exception("User does not exist!");
            if(UserType.USER == user.getUserType()) throw new Exception("User does not have privileges");
            Iterator<UnmoderatedSighting> sightings = unmoderatedSightingRepository.findAll().iterator();
            List<Bird> birds = IterableUtils.toList(birdService.fetchAllBirds());
            List<com.uol.birding.dto.Sighting> sightingList = new ArrayList<>();
            while(sightings.hasNext()) {
                UnmoderatedSighting sighting = sightings.next();
                Bird bird = birds
                        .stream()
                        .filter(b -> b.getId().equals(sighting.getBirdId()))
                        .findFirst().orElseThrow(() -> new Exception("Bird not found!"));
                com.uol.birding.dto.Bird birdDto = com.uol.birding.dto.Bird
                        .builder()
                        .commonName(bird.getCommonName())
                        .scientificName(bird.getScientificName())
                        .classification(bird.getClassification())
                        .taxonomicGrouping(bird.getTaxonomicGrouping())
                        .family(bird.getFamily())
                        .imageRef(bird.getImageRef())
                        .build();
                User user1 = userService.findUser(sighting.getObserverId());
                com.uol.birding.dto.Sighting sightingDto = com.uol.birding.dto.Sighting
                        .builder()
                        .id(sighting.getId())
                        .creator(user1)
                        .bird(birdDto)
                        .latitude(sighting.getLatitude())
                        .longitude(sighting.getLongitude())
                        .quantity(sighting.getQuantity())
                        .date(sighting.getDate())
                        .imageRef(sighting.getImageRef())
                        .build();
                sightingList.add(sightingDto);

            }
            return ResponseEntity.ok(sightingList);
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity approveSightings(String userName, List<Integer> unmoderatedIds) {
        try {
            User user = userService.findUser(userName);
            if(user==null) throw new Exception("User does not exist!");
            if(UserType.USER == user.getUserType()) throw new Exception("User does not have authority!");
            unmoderatedIds.forEach((id) -> {
                UnmoderatedSighting unmoderatedSighting = unmoderatedSightingRepository.findById(id).get();
                Sighting sighting = Sighting
                        .builder()
                        .observerId(unmoderatedSighting.getObserverId())
                        .birdId(unmoderatedSighting.getBirdId())
                        .latitude(unmoderatedSighting.getLatitude())
                        .longitude(unmoderatedSighting.getLongitude())
                        .quantity(unmoderatedSighting.getQuantity())
                        .date(unmoderatedSighting.getDate())
                        .imageRef(unmoderatedSighting.getImageRef())
                        .approverId(userName)
                        .build();
                sightingRepository.save(sighting);
                unmoderatedSightingRepository.delete(unmoderatedSighting);
            });
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"success\":\"Sightings has been approved\"}");
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity discardSightings(String userName, List<Integer> unmoderatedIds) {
        try {
            User user = userService.findUser(userName);
            if(user==null) throw new Exception("User does not exist!");
            if(UserType.USER == user.getUserType()) throw new Exception("User does not have authority!");
            unmoderatedIds.forEach((id) -> {
                UnmoderatedSighting unmoderatedSighting = unmoderatedSightingRepository.findById(id).get();
                unmoderatedSightingRepository.delete(unmoderatedSighting);
            });
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

}
