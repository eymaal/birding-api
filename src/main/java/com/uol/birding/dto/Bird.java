package com.uol.birding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Bird {
    private String commonName;
    private String scientificName;
    private String classification;
    private String taxonomicGrouping;
    private String family;
    private String imageRef;
}
