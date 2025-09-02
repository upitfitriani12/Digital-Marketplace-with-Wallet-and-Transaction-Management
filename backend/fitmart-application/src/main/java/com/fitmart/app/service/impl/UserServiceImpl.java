package com.fitmart.app.service.impl;

import com.fitmart.app.entity.User;
import com.fitmart.app.repository.UserRepository;
import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.UserService;
import com.fitmart.app.utils.GeneralSpecification;
import com.fitmart.app.utils.dto.request.LoginUserRequest;
import com.fitmart.app.utils.dto.request.RegisterUserRequest;
import com.fitmart.app.utils.dto.response.LoginUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository user_repository;
    private final JwtUtils jwtUtils;

    @Override
    public User create(RegisterUserRequest request) {
        User new_user = RegisterUserRequest.fromRegisterToUserMapper(request);
        String hashedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
        new_user.setPassword(hashedPassword);
        return user_repository.saveAndFlush(new_user);
    }

    @Override
    public LoginUserResponse login(LoginUserRequest request) {
        LoginUserResponse loginResponse = new LoginUserResponse();
        loginResponse.setAccess_token("");
        try {
            User user = user_repository.findByEmail(request.getEmail());
            if(user.getEmail() != null) {
                Boolean isPasswordMatch = new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword());
                if(new BCryptPasswordEncoder().matches(request.getPassword(), user.getPassword())) {
                    String accessToken = jwtUtils.generateAccessToken(user);
                    loginResponse.setAccess_token(accessToken);
                }
            }
            return loginResponse;
        } catch (Exception error) {
            return loginResponse;
        }
    }

    @Override
    public User getById(String id) {
        return user_repository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "User with id " + id + " is not found"));
    }

    @Override
    public Page<User> getAll(Pageable pageable, RegisterUserRequest registerUserRequest) {
        Specification<User> specification = GeneralSpecification.getSpecification(registerUserRequest);
        return user_repository.findAll(specification,pageable);
    }

    @Override
    public User update(RegisterUserRequest request) {
        User existing_user = user_repository.findById(request.getId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "User with id " + request.getId() + " is not found"));
        existing_user.setName(request.getName());
        existing_user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            String hashedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
            existing_user.setPassword(hashedPassword);
        }

        return user_repository.saveAndFlush(existing_user);
    }

    @Override
    public void delete(String id) {
        this.getById(id);
        user_repository.deleteById(id);
    }
}

