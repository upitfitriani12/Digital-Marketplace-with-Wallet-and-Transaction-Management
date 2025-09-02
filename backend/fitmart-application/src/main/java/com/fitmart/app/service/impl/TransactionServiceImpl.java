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
    public Transaction create(TransactionRequest request, String id) {
        Wallet wallet = walletService.fineByUserId(id);
        String product_id = request.getProduct_id();
        Product product = productService.getById(product_id);
        WalletRequest wallet_request = new WalletRequest();
        Integer product_price = product.getPrice();
        Integer balance = wallet.getBalance();

        if (request.getDate_start() != null && request.getDate_end() != null) {
            long diffInMillies = Math.abs(request.getDate_end().getTime() - request.getDate_start().getTime());
            int diff = (int) (diffInMillies / (1000 * 60 * 60 * 24));
            Integer total_price = diff * product_price * request.getQuantity();

            if (balance >= total_price && id.equals(wallet.getUser().getId())) {
                wallet_request.setId(wallet.getId());
                wallet_request.setBalance(wallet.getBalance());
                wallet_request.setUser_id(id);
                wallet_request.setBalance(balance - total_price);
                request.setUser_id(id);
                walletService.update(wallet_request);

                request.setTotal(total_price);

                Transaction transaction = request.convert();
                transaction.setDuration(Integer.valueOf(String.valueOf(diff)));
                return transactionRepository.saveAndFlush(transaction);
            }
            throw new NullPointerException("TOP UP DULU BOS");

        } else {
            throw new IllegalArgumentException("DateStart and DateEnd cannot be null");
        }
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
    public Transaction update(TransactionRequest request) {

        Integer product_price = request.getPrice_history();
        Transaction total_lama = this.getById(request.getId());
        Wallet wallet_baru = walletService.fineByUserId(request.getUser_id());

        long diffInMillies = Math.abs(request.getDate_end().getTime() - request.getDate_start().getTime());
        int diff = (int) (diffInMillies / (1000 * 60 * 60 * 24));
        Integer total_baru = diff * product_price * request.getQuantity();
        Integer baru = total_baru - total_lama.getTotal();

        if (wallet_baru.getBalance() - baru >= 0) {

            wallet_baru.setBalance(wallet_baru.getBalance() - baru);
            request.setUser_id(request.getUser_id());
            request.setTotal(total_baru);
            return transactionRepository.saveAndFlush(request.convert());

        }

        throw new NullPointerException("TOP UP DULU BOS");


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
