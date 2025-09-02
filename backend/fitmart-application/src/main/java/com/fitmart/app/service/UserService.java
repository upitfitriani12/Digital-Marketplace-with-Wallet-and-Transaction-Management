package com.fitmart.app.service;

import com.fitmart.app.entity.User;
import com.fitmart.app.utils.dto.request.LoginUserRequest;
import com.fitmart.app.utils.dto.request.RegisterUserRequest;
import com.fitmart.app.utils.dto.response.LoginUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User create(RegisterUserRequest request);
    LoginUserResponse login(LoginUserRequest request);
    User getById(String id);
    Page<User> getAll(Pageable pageable,RegisterUserRequest registerUserRequest);
    User update(RegisterUserRequest request);
    void delete(String id);
}

