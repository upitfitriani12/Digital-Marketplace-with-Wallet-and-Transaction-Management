package com.fitmart.app.service;

import java.util.List;

import com.fitmart.app.entity.Transaction;
import com.fitmart.app.utils.dto.request.TransactionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    Transaction create(TransactionRequest request, String id);
    Page<Transaction> getAll(Pageable pageable, TransactionRequest request);
    Transaction getById(String id);
    Transaction update(TransactionRequest request);
    void delete(String id);
    List<Transaction> findByUserId(String userId);

}
