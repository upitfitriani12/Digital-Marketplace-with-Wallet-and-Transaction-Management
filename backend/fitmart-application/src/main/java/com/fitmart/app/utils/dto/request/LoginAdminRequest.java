package com.fitmart.app.utils.dto.request;

import com.fitmart.app.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Data
public class LoginAdminRequest {
    private String email;
    private String password;

    public static Admin fromLoginRequestToAdminMapper(LoginAdminRequest loginAdminRequest){
        return Admin.builder()
                .email(loginAdminRequest.email)
                .password(loginAdminRequest.password)
                .build();


    }

}
