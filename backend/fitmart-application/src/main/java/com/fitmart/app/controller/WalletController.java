package com.fitmart.app.controller;


import com.fitmart.app.entity.Wallet;
import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.AdminService;
import com.fitmart.app.service.UserService;
import com.fitmart.app.service.WalletService;
import com.fitmart.app.utils.dto.request.WalletRequest;
import com.fitmart.app.utils.dto.response.WalletResponse;
import com.fitmart.app.utils.dto.webResponse.PageResponse;
import com.fitmart.app.utils.dto.webResponse.Res;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody WalletRequest request, @RequestHeader(name = "Authorization") String access_token) {

        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            Wallet wallet = walletService.create(request);
            WalletResponse response = WalletResponse.fromWallet(wallet);
            return Res.renderJson(response, "wallet ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(name = "Authorization") String access_token,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
            @ModelAttribute WalletRequest request) {

        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            PageResponse<WalletResponse> res = new PageResponse<>(WalletResponse.convertToUserResponsePage(walletService.getAll(page,request)));
            return Res.renderJson(res, "ok", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }

    }

    @GetMapping(path = "/me")
    public ResponseEntity<?> getByMe(@RequestHeader(name = "Authorization") String access_token) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(userService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(WalletResponse.fromWallet(walletService.fineByUserId(userService.getById(jwtPayload.getSubject()).getId())), "wallet ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");}
    }

    @GetMapping(path = "/{id_wallet}")
    public ResponseEntity<?> getById(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id_wallet) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(WalletResponse.fromWallet(walletService.getById(id_wallet)), "wallet ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");}
    }



    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestHeader(name = "Authorization") String access_token, @RequestBody WalletRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            Wallet updatedProduct = walletService.update(request);
            return ResponseEntity.ok(WalletResponse.fromWallet(updatedProduct));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }


    @DeleteMapping(path = "/{id_wallet}")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id_wallet) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            try {
                walletService.delete(id_wallet);
                return Res.renderJson(null, "Wallet Deleted Successfully", HttpStatus.OK);
            } catch (Exception e) {
                return Res.renderJson(null, "Failed to Delete Wallet", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }

    }

}