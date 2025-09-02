package com.fitmart.app.controller;

import com.fitmart.app.entity.Transaction;
import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.AdminService;
import com.fitmart.app.service.TransactionService;
import com.fitmart.app.service.UserService;
import com.fitmart.app.utils.dto.request.TransactionRequest;
import com.fitmart.app.utils.dto.response.TransactionResponse;
import com.fitmart.app.utils.dto.webResponse.PageResponse;
import com.fitmart.app.utils.dto.webResponse.Res;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;



@AllArgsConstructor
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AdminService adminService;


    @PostMapping
    public ResponseEntity<?> create(@RequestHeader(name = "Authorization")String access_token, @RequestBody TransactionRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String userIdFromToken = jwtPayload.getSubject();
        String userId = userService.getById(userIdFromToken).getId();
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(userId);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());


        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            TransactionResponse response = TransactionResponse.fromTransaction(transactionService.create(request, userIdFromToken));
            return  Res.renderJson(response, "Transaction Created Successfully", HttpStatus.CREATED);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }

    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(name = "Authorization") String access_token,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
            @ModelAttribute TransactionRequest request) {

        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String userIdFromToken = jwtPayload.getSubject();
        String adminId = adminService.getById(userIdFromToken).getId();
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(adminId);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            PageResponse<TransactionResponse> res = new PageResponse<>(TransactionResponse.convertToUserResponsePage(transactionService.getAll(page,request)));
            return Res.renderJson(res, "ok", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }

    }


    @GetMapping(path = "/{id_transaction}")
    public ResponseEntity<?> getById(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id_transaction) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String userIdFromToken = jwtPayload.getSubject();
        String admin_id = adminService.getById(userIdFromToken).getId();
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(admin_id);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            TransactionResponse response = TransactionResponse.fromTransaction(transactionService.getById(id_transaction));
            return Res.renderJson(response, "Transaction ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");}
    }


    @GetMapping(path = "/me")
    public ResponseEntity<?> getByMe(@RequestHeader(name = "Authorization") String access_token) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String userIdFromToken = jwtPayload.getSubject();
        String user_id = userService.getById(userIdFromToken).getId();
        boolean isUserIdJWTequalsUserIdReqParams = userIdFromToken.equals(user_id);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isUserIdJWTequalsUserIdReqParams && isTokenNotYetExpired) {
            List<TransactionResponse> res = new ArrayList<>(TransactionResponse.confertListTransaction(transactionService.findByUserId(userIdFromToken)));
            return Res.renderJson(res, "Transaction ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");}
    }




    @PutMapping(path = "/update")
    public ResponseEntity<?> update(@RequestHeader(name = "Authorization") String access_token, @RequestBody TransactionRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        String adminIdFromToken = jwtPayload.getSubject();
        String adminId = adminService.getById(adminIdFromToken).getId();
        boolean isProductIdJWTequalsProductIdReqParams = adminIdFromToken.equals(adminId);
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            Transaction updatedProduct = transactionService.update(request);
            return ResponseEntity.ok(TransactionResponse.fromTransaction(updatedProduct));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }


    @DeleteMapping(path = "/{idTransaction}")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String access_token, @PathVariable String idTransaction) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            try {
                transactionService.delete(idTransaction);
                return Res.renderJson(null, "Transaction Deleted Successfully", HttpStatus.OK);
            } catch (Exception e) {
                return Res.renderJson(null, "Failed to Delete Transaction", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }

    }

}
