package com.uol.birding.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.uol.birding.dto.User;
import com.uol.birding.entity.Bird;
import com.uol.birding.enums.UserType;
import com.uol.birding.repository.BirdRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BirdServiceTest {

    private MockMvc mockMvc;
    private MockMvc userMockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @InjectMocks
    private BirdService birdService;

    @InjectMocks
    private UserService userService;

    @Mock
    private BirdRepository birdRepository;

    private List<Bird> birds;

    @BeforeEach
    public void setUp() {
        Bird b = Bird.builder().build();
        birds.add(b);
        MockitoAnnotations.initMocks(this);
        // Mock the behavior of the BirdRepository
        when(birdRepository.findAll()).thenReturn(birds);
        this.mockMvc = MockMvcBuilders.standaloneSetup(birdService).build();
        this.userMockMvc = MockMvcBuilders.standaloneSetup(userService).build();
    }

    @Test
    public void testGetAllBirdsSuccess() {

        birdService = new BirdService(birdRepository, null);
        // Call the getAllBirds method
        ResponseEntity responseEntity = birdService.getAllBirds();

        // Assert the expected ResponseEntity status code (200 OK)
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetAllBirdsError() {
        // Mock an exception in the BirdRepository
        when(birdRepository.findAll()).thenThrow(new RuntimeException("Test Exception"));

        // Call the getAllBirds method
        ResponseEntity responseEntity = birdService.getAllBirds();

        // Assert the expected ResponseEntity status code (500 Internal Server Error)
        assertEquals(400, responseEntity.getStatusCodeValue());

    }

    @Test
    public void testFindBirdByCommonName() {
        String commonName = "";
        Bird b = Bird.builder().build();
        when(birdRepository.findByCommonName(commonName)).thenReturn(Optional.ofNullable(b));

        assertEquals(b, birdService.findBirdByCommonName(commonName));
    }

//    @Test
//    public void testAddNewBird() throws Exception {
//        Bird b = Bird.builder().build();
//        User user = User.builder()
//                        .userType(UserType.ADMIN)
//                        .build();
//        String userName = "";
//        when(userService.findUser(userName)).thenReturn(user);
//        String commonName = "";
//        when(birdRepository.findByCommonName(commonName)).thenReturn(Optional.ofNullable(b));
//        assertEquals(201,birdService.addNewBird(userName, b).getStatusCode());
//    }
}
