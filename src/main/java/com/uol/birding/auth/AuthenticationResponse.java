//referred https://www.youtube.com/watch?v=lA18U8dGKF8&t=1033s
package com.uol.birding.auth;

import com.uol.birding.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private UserType userType;
    private String name;
}
