package com.fitmart.app.controller;

import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.AdminService;
import com.fitmart.app.service.UserService;
import com.fitmart.app.utils.dto.request.LoginUserRequest;
import com.fitmart.app.utils.dto.request.RegisterUserRequest;
import com.fitmart.app.utils.dto.response.LoginUserResponse;
import com.fitmart.app.utils.dto.response.UserResponse;
import com.fitmart.app.utils.dto.webResponse.PageResponse;
import com.fitmart.app.utils.dto.webResponse.Res;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final AdminService adminService;
    private final UserService user_service;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse response = UserResponse.fromUser(user_service.create(request));
        return Res.renderJson(response, "Register User Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public LoginUserResponse login(@RequestBody LoginUserRequest request) {
        return user_service.login(request);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getByUser(
            @RequestHeader(name = "Authorization") String access_token,
            @PathVariable String id
    ) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String userIdFromToken = jwtPayload.getSubject();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(id);

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(
                    UserResponse.fromUser(user_service.getById(id)),
                    "User ID Retrieved Successfully",
                    HttpStatus.OK
            );
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestHeader(name = "Authorization") String access_token,
            @RequestBody RegisterUserRequest request
    ) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();

        String userIdFromToken = jwtPayload.getSubject();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(request.getId());

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            UserResponse updatedUser = UserResponse.fromUser(user_service.update(request));
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to update user");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(
            @RequestHeader(name = "Authorization") String access_token,
            @PathVariable String id
    ) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();

        String userIdFromToken = jwtPayload.getSubject();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        boolean isUserDeletingOwnAccount = userIdFromToken.equals(id);

        if (isUserDeletingOwnAccount && isTokenNotYetExpired) {
            try {
                user_service.delete(id);
                return Res.renderJson(null, "User deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return Res.renderJson(null, "Failed to delete user", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Forbidden: you can only delete your own account.");
        }
    }

}
