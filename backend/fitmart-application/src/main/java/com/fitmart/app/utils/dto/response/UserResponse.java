package com.fitmart.app.utils.dto.response;
import java.util.List;
import java.util.stream.Collectors;

import com.fitmart.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private String id;
    private String email;

    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    public static  Page<UserResponse> convertToUserResponsePage(Page<User> userPage) {
        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());

        return new PageImpl<>(userResponses, userPage.getPageable(), userPage.getTotalElements());
    }




}
