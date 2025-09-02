package com.fitmart.app.service;

import com.fitmart.app.entity.Admin;
import com.fitmart.app.utils.dto.request.LoginAdminRequest;
import com.fitmart.app.utils.dto.request.RegisterAdminRequest;
import com.fitmart.app.utils.dto.response.LoginAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Admin create(RegisterAdminRequest request);
    LoginAdminResponse login(LoginAdminRequest request);
    Admin getById(String id);
    Page<Admin> getAll(Pageable pageable, RegisterAdminRequest registerAdminRequest);
    Admin update(RegisterAdminRequest request);
    void delete(String id);
}
