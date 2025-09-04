package com.fitmart.app.service;

import com.fitmart.app.entity.Wallet;

import com.fitmart.app.utils.dto.request.WalletRequest;

public interface WalletService {
    Wallet create(WalletRequest request, String userId);
    Wallet findByUserId(String userId);
    Wallet update(WalletRequest request, String userId);
}

