package com.uol.birding.service;

import com.uol.birding.dto.Bird;
import com.uol.birding.dto.BirdStat;
import com.uol.birding.dto.User;
import com.uol.birding.dto.UserStat;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.BirdRepository;
import com.uol.birding.repository.SightingRepository;
import com.uol.birding.repository.UserRepository;
import com.uol.birding.util.BirdingMessageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatService {

    private final BirdRepository birdRepository;
    private final SightingRepository sightingRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ResponseEntity getBirdStats() {
        try {
            List<String> statString = sightingRepository.getTop3SightedBirds();
            List<BirdStat> birdStats = new ArrayList<>();
            statString.forEach(str -> {
                String[] tempStr = str.split(",");
                com.uol.birding.entity.Bird b = birdRepository.findById(Integer.valueOf(tempStr[0])).orElse(null);
                Bird bird = Bird
                        .builder()
                        .commonName(b.getCommonName())
                        .scientificName(b.getScientificName())
                        .family(b.getFamily())
                        .classification(b.getClassification())
                        .taxonomicGrouping(b.getTaxonomicGrouping())
                        .imageRef(b.getImageRef())
                        .build();
                BirdStat stat = BirdStat
                        .builder()
                        .bird(bird)
                        .quantity(Integer.valueOf(tempStr[1]))
                        .build();
                birdStats.add(stat);
            });
            return ResponseEntity
                    .ok(birdStats);
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity getUserStats() {
        try {
            List<String> userStatsString = sightingRepository.getTop3Observers();
            List<UserStat> userStats = new ArrayList<>();
            userStatsString.forEach(row -> {
                String[] tempStr = row.split(",");
                try {
                    User user = userService.findUser(tempStr[0]);
                    UserStat userStat = UserStat
                            .builder()
                            .user(user)
                            .submissions(Integer.valueOf(tempStr[1]))
                            .build();
                    userStats.add(userStat);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return ResponseEntity
                    .ok(userStats);
        } catch (Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }

    public ResponseEntity getPrivilegedStats(String username) {
        try{
            if(UserType.USER==userService.findUser(username).getUserType()) throw new Exception("User does not have authority");
            Map<String, Long> statMap = new HashMap<>();
            statMap.put("SIGHTING",sightingRepository.count());
            Arrays.stream(UserType.values()).forEach(type -> {
                statMap.put(type.name(), userRepository.countUsersByUserType(type));
            });
            return ResponseEntity
                    .ok(statMap);
        } catch(Exception e) {
            return BirdingMessageUtils.createErrorResponse(e.getMessage());
        }
    }
}
