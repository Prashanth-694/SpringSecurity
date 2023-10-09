package com.eidiko.security.bo;

import lombok.Data;

@Data
public class JwtRequest {
private String username;
private String password;
}
