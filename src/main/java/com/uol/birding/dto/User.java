package com.uol.birding.dto;

import com.uol.birding.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    private String username;
    private String name;
    private UserType userType;
    private Boolean locked;
}
