package com.app.request;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSigninRequest {
    private String email;
    private String password;
}

