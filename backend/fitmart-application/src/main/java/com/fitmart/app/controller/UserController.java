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
    public ResponseEntity<?> getOne(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isUserIdJWTequalsUserIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(UserResponse.fromUser(user_service.getById(id)), "User ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(name = "Authorization") String access_token,
            @PageableDefault(page = 0,size = 10,sort = "id",direction = Sort.Direction.ASC) Pageable page,
            @ModelAttribute RegisterUserRequest registerUserRequest
    ){
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String getToken = jwtPayload.getSubject();
        String getAdmin = adminService.getById(getToken).getId();
        boolean isAdminIdJWTEqualsAdminIdReqParams = jwtPayload.getSubject().equals(getAdmin);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isAdminIdJWTEqualsAdminIdReqParams && isTokenNotYetExpired){
            PageResponse<UserResponse> res = new PageResponse<>(UserResponse.convertToUserResponsePage(user_service.getAll(page,registerUserRequest)));
            return Res.renderJson(res,"ok",HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestHeader(name = "Authorization") String access_token, @RequestBody RegisterUserRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String userIdFromToken = jwtPayload.getSubject();
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(adminService.getById(userIdFromToken).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            UserResponse updatedUser = UserResponse.fromUser(user_service.update(request));
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String getAdmin = adminService.getById(jwtPayload.getSubject()).getId();
        boolean isUserIdJWTequalsUserIdReqParams = jwtPayload.getSubject().equals(getAdmin);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            try {
                user_service.delete(id);
                return Res.renderJson(null, "User Deleted Successfully", HttpStatus.OK);
            } catch (Exception e) {
                return Res.renderJson(null, "Failed to Delete User", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }


}
