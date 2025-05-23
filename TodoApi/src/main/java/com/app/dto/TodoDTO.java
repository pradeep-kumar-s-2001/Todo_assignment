package com.app.dto;



import com.app.entity.Users;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Users user;
    
}

