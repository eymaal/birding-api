package com.uol.birding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bird {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String commonName;
    @Column(nullable = false)
    private String scientificName;
    private String classification;
    @Column(nullable = false)
    private String taxonomicGrouping;
    @Column(nullable = false)
    private String family;
    private String avibase;
    private String imageRef;
}
