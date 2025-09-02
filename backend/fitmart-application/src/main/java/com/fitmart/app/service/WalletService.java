package com.fitmart.app.service;

import com.fitmart.app.entity.Wallet;

import com.fitmart.app.utils.dto.request.WalletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WalletService {
    Wallet create(WalletRequest request);
    Page<Wallet> getAll(Pageable pageable, WalletRequest request);
    Wallet getById(String id);
    Wallet update(WalletRequest request);
    void delete(String id);
    Wallet fineByUserId(String userId);
}
