package com.fitmart.app.controller;
import com.fitmart.app.entity.Category;
import com.fitmart.app.security.JwtUtils;
import com.fitmart.app.service.AdminService;
import com.fitmart.app.service.CategoryService;
import com.fitmart.app.utils.dto.request.CategoryRequest;
import com.fitmart.app.utils.dto.response.CategoryResponse;
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
@RequestMapping("/categories")

public class CategoryController {

    private final AdminService adminService;
    private final CategoryService categoryService;
    private final JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoryRequest request, @RequestHeader(name = "Authorization") String accessToken) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdmin = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isAdmin && isTokenValid) {
            Category category = categoryService.create(request);
            CategoryResponse response = CategoryResponse.fromCategory(category);
            return Res.renderJson(response, "Category created successfully", HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or token expired");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestHeader(name = "Authorization") String accessToken,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @ModelAttribute CategoryRequest request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isTokenValid) {
            PageResponse<Category> res = new PageResponse<>(categoryService.getAll(pageable, request));
            return Res.renderJson(res, "Categories retrieved successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader(name = "Authorization") String accessToken, @PathVariable String id) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdmin = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isAdmin && isTokenValid) {
            CategoryResponse response = CategoryResponse.fromCategory(categoryService.getById(id));
            return Res.renderJson(response, "Category retrieved successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or token expired");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestHeader(name = "Authorization") String accessToken, @RequestBody Category request) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdmin = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isAdmin && isTokenValid) {
            Category updatedCategory = categoryService.update(request);
            return ResponseEntity.ok(CategoryResponse.fromCategory(updatedCategory));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or token expired");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization") String accessToken, @PathVariable String id) {
        Claims jwtPayload = jwtUtils.decodeAccessToken(accessToken);
        Date currentDate = new Date();
        boolean isAdmin = jwtPayload.getSubject().equals(adminService.getById(jwtPayload.getSubject()).getId());
        boolean isTokenValid = currentDate.before(jwtPayload.getExpiration());
        if (isAdmin && isTokenValid) {
            try {
                categoryService.delete(id);
                return Res.renderJson(null, "Category deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return Res.renderJson(null, "Failed to delete category", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied or token expired");
        }
    }

}
