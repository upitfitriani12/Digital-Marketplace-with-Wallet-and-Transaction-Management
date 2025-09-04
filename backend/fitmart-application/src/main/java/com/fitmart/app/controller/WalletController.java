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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody WalletRequest request,
                                    @RequestHeader(name = "Authorization") String access_token) {

        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isTokenNotYetExpired) {
            String userId = jwtPayload.getSubject();

            Wallet wallet = walletService.create(request, userId);
            WalletResponse response = WalletResponse.fromWallet(wallet);

            return Res.renderJson(response, "wallet ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<?> getMyWallet(@RequestHeader(name = "Authorization") String access_token) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isTokenNotYetExpired) {
            String userId = jwtPayload.getSubject();

            Wallet wallet = walletService.findByUserId(userId);
            WalletResponse response = WalletResponse.fromWallet(wallet);

            return Res.renderJson(response, "Wallet Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> updateMyWallet(@RequestHeader(name = "Authorization") String access_token,
                                            @RequestBody WalletRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isTokenNotYetExpired) {
            String userId = jwtPayload.getSubject();

            Wallet updatedWallet = walletService.update(request, userId);
            WalletResponse response = WalletResponse.fromWallet(updatedWallet);

            return Res.renderJson(response, "Wallet Updated Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }

}