package com.fitmart.app.service.impl;

import com.fitmart.app.entity.Product;
import com.fitmart.app.entity.Transaction;
import com.fitmart.app.entity.Wallet;
import com.fitmart.app.repository.TransactionRepository;
import com.fitmart.app.service.ProductService;
import com.fitmart.app.service.TransactionService;
import com.fitmart.app.service.WalletService;
import com.fitmart.app.utils.GeneralSpecification;
import com.fitmart.app.utils.dto.request.TransactionRequest;
import com.fitmart.app.utils.dto.request.WalletRequest;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final ProductService productService;

    @Override
    public Transaction create(TransactionRequest request, String userId) {
        Wallet wallet = walletService.findByUserId(userId);
        Product product = productService.getById(request.getProduct_id());

        Integer totalPrice = product.getPrice() * request.getQuantity();

        if (wallet.getBalance() < totalPrice) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Insufficient balance, please top up first");
        }

        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setId(wallet.getId());
        walletRequest.setUserId(userId);
        walletRequest.setBalance(wallet.getBalance() - totalPrice);
        walletService.update(walletRequest, userId);

        request.setUser_id(userId);
        request.setTotal(totalPrice);

        Transaction transaction = request.convert();

        transaction.setUser(wallet.getUser());
        transaction.setProduct(product);

        return transactionRepository.saveAndFlush(transaction);
    }


    @Override
    public Page<Transaction> getAll(Pageable pageable, TransactionRequest request) {
        Specification<Transaction> specification = GeneralSpecification.getSpecification(request);
        return transactionRepository.findAll(specification, pageable);
    }

    @Override
    public Transaction getById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "User with id " + id + " is not found"));
    }

    @Override
    public void delete(String id) {
        this.getById(id);
        transactionRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findByUserId(String userId) {
        return transactionRepository.findByUserId(userId);

    }
}
