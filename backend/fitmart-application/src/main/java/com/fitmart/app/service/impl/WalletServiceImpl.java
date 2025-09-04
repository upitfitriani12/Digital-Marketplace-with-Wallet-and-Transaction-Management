package com.fitmart.app.service.impl;

import com.fitmart.app.entity.User;
import com.fitmart.app.entity.Wallet;
import com.fitmart.app.repository.UserRepository;
import com.fitmart.app.repository.WalletRepository;
import com.fitmart.app.service.WalletService;
import com.fitmart.app.utils.dto.request.WalletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Override
    public Wallet create(WalletRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        walletRepository.findByUser_Id(userId).ifPresent(w -> {
            throw new RuntimeException("Wallet already exists for this user");
        });

        Wallet wallet = request.convert();
        wallet.setUser(user);

        return walletRepository.saveAndFlush(wallet);
    }

    @Override
    public Wallet findByUserId(String userId) {
        return walletRepository.findByUser_Id(userId)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND,
                        "Wallet for user " + userId + " is not found"));
    }

    @Override
    public Wallet update(WalletRequest request, String userId) {
        Wallet wallet = walletRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(request.getBalance());
        return walletRepository.saveAndFlush(wallet);
    }

}
