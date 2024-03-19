package com.uol.birding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SightingRequest {
    private String userEmail;
    private String birdSpecies;
    private String latitude;
    private String longitude;
    private Integer quantity;
    private Date date;
    private String imageRef;
}
