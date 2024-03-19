package com.uol.birding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnmoderatedSighting {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private Integer birdId;
    @Column(nullable = false)
    private String observerId;
    @Column(nullable = false)
    private String latitude;
    @Column(nullable = false)
    private String longitude;
    private Integer quantity=1;
    @Column(nullable = false)
    private Date date;
    private String imageRef;
}
