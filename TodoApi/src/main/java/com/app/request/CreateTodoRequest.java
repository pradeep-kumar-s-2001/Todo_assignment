package com.app.request;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTodoRequest {
    private String title;
    private Long userId; // To associate with the user
}
