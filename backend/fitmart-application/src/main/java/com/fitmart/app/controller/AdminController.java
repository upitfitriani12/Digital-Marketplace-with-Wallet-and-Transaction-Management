package com.fitmart.app.controller;

import com.fitmart.app.entity.Admin;
import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.AdminService;
import com.fitmart.app.service.UserService;
import com.fitmart.app.utils.dto.request.LoginAdminRequest;
import com.fitmart.app.utils.dto.request.RegisterAdminRequest;
import com.fitmart.app.utils.dto.request.RegisterUserRequest;
import com.fitmart.app.utils.dto.response.AdminResponse;
import com.fitmart.app.utils.dto.response.LoginAdminResponse;
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

@RestController
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterAdminRequest request) {
        AdminResponse adminResponse = AdminResponse.fromAdmin(adminService.create(request));
        return Res.renderJson(adminResponse,"Register Admin created successfully",HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public LoginAdminResponse login(@RequestBody LoginAdminRequest request){
        return adminService.login(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable String id,
            @RequestHeader(name = "Authorization") String accessToken
    ){
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();

        boolean isAdminIdJWTEqualsAdminIdReqParams = jwtPayload.getSubject().equals(id);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isAdminIdJWTEqualsAdminIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(
                    adminService.getById(id),
                    "Admin ID Retrieved Successfully",
                    HttpStatus.OK
            );
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<?> getByIdUserFromAdmin(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isUserIdJWTequalsUserIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(UserResponse.fromUser(userService.getById(id)), "User ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(name = "Authorization") String accessToken,
            @PageableDefault(page = 0,size = 10,sort = "id",direction = Sort.Direction.ASC) Pageable page,
            @ModelAttribute RegisterAdminRequest registerAdminRequest
    ){
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        String getToken = jwtPayload.getSubject();
        String getAdmin = adminService.getById(getToken).getId();
        boolean isAdminIdJWTEqualsAdminIdReqParams = jwtPayload.getSubject().equals(getAdmin);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        if (isAdminIdJWTEqualsAdminIdReqParams && isTokenNotYetExpired){
            PageResponse<Admin> res = new PageResponse<>(adminService.getAll(page, registerAdminRequest));
            return Res.renderJson(res, "ok", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }

    @GetMapping(path = "/users")
    public ResponseEntity<?> getAllUserByAdmin(
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
            PageResponse<UserResponse> res = new PageResponse<>(UserResponse.convertToUserResponsePage(userService.getAll(page,registerUserRequest)));
            return Res.renderJson(res,"ok",HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestHeader(name = "Authorization") String accessToken,
            @RequestBody RegisterAdminRequest request
    ){
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdminIdJWTEqualsAdminIdReqParams = jwtPayload.getSubject().equals(request.getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());
        if (isAdminIdJWTEqualsAdminIdReqParams && isTokenNotYetExpired) {
            Admin admin = adminService.update(request);
            return ResponseEntity.ok(AdminResponse.fromAdmin(admin));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id,
            @RequestHeader(name = "Authorization") String accessToken
    ){
        Claims jwtPayLoad = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdminIdJWTEqualsAdminIdReqParams = jwtPayLoad.getSubject().equals(adminService.getById(jwtPayLoad.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayLoad.getExpiration());
        if (isAdminIdJWTEqualsAdminIdReqParams && isTokenNotYetExpired){
            try {
                adminService.delete(id);
                return Res.renderJson(null,"Admin deleted successfully",HttpStatus.OK);
            } catch (Exception error){
                return Res.renderJson(null,"Failed to delete admin",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Failed to Find");
        }
    }
}
