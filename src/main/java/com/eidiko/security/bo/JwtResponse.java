package com.eidiko.security.bo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
private String jwtToken;
private String username;
private String refreshToken;
}
