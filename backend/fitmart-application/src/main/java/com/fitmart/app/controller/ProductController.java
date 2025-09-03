package com.fitmart.app.controller;

import com.fitmart.app.entity.Product;
import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.AdminService;
import com.fitmart.app.service.ProductService;
import com.fitmart.app.utils.dto.request.ProductRequest;
import com.fitmart.app.utils.dto.response.ProductResponse;
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
@RequestMapping("/products")
public class ProductController {

    private final AdminService adminService;
    private final ProductService productService;
    private final JwtUtils jwtUtils;

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody ProductRequest request, @RequestHeader(name = "Authorization") String accessToken) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdmin = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isAdmin && isTokenValid) {
            Product product = productService.create(request);
            ProductResponse response = ProductResponse.fromProduct(product);
            return Res.renderJson(response, "Product created successfully", HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }


    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(name = "Authorization") String access_token,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
            @ModelAttribute ProductRequest request) {

        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isTokenNotYetExpired) {
            PageResponse<Product> res = new PageResponse<>(productService.getAll(page, request));
            return Res.renderJson(res, "ok", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired");
        }

    }

    @GetMapping(path = "/{id_product}")
    public ResponseEntity<?> getById(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id_product) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            return Res.renderJson(ProductResponse.fromProduct(productService.getById(id_product)), "product ID Retrieved Successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");}
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader(name = "Authorization") String accessToken,
                                    @PathVariable String id,
                                    @Valid @RequestBody ProductRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdmin = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isAdmin && isTokenValid) {
            Product updatedProduct = productService.update(id, request);
            return ResponseEntity.ok(ProductResponse.fromProduct(updatedProduct));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }
    }

    @DeleteMapping(path = "/{id_product}")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String access_token, @PathVariable String id_product) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(access_token);
        Date currentDate = new Date();
        boolean isProductIdJWTequalsProductIdReqParams = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenNotYetExpired = currentDate.before(jwtPayload.getExpiration());

        if (isProductIdJWTequalsProductIdReqParams && isTokenNotYetExpired) {
            try {
                productService.delete(id_product);
                return Res.renderJson(null, "Product Deleted Successfully", HttpStatus.OK);
            } catch (Exception e) {
                return Res.renderJson(null, "Failed to Delete Product", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or Token expired");
        }

    }
}

