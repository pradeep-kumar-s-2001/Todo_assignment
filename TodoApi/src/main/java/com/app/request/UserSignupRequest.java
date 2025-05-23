package com.app.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequest {
    private String name;
    private String email;
    private String password;
}
