package com.fitmart.app.utils.dto.request;

import com.fitmart.app.entity.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class LoginUserRequest {

    private String email;
    private String password;

    public static User fromLoginRequestToUserMapper(LoginUserRequest loginUserRequest){
        return User.builder()
                .email(loginUserRequest.getEmail())
                .password(loginUserRequest.password)
                .build();
    }
}

