package com.fitmart.app.service.impl;

import com.fitmart.app.entity.Wallet;
import com.fitmart.app.repository.WalletRepository;
import com.fitmart.app.service.WalletService;
import com.fitmart.app.utils.GeneralSpecification;
import com.fitmart.app.utils.dto.request.WalletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet create(WalletRequest request) {
        return walletRepository.saveAndFlush(request.convert());
    }

    @Override
    public Page<Wallet> getAll(Pageable pageable, WalletRequest request) {
        Specification<Wallet> specification =   GeneralSpecification.getSpecification(request);
        return walletRepository.findAll(specification, pageable);
    }

    @Override
    public Wallet getById(String id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Wallet with id " + id + " is not found"));
    }

    public Wallet fineByUserId(String userId) {
        return walletRepository.findByUserId(userId);

    }

    @Override
    public Wallet update(WalletRequest request) {
        Integer hasil = request.getBalance() + getById(request.getId()).getBalance();
        request.setBalance(hasil);
        return walletRepository.saveAndFlush(request.convert());
    }

    @Override
    public void delete(String id) {
        this.getById(id);
        walletRepository.deleteById(id);
    }
}

