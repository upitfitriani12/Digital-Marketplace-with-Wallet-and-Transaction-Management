package com.fitmart.app.utils.dto.request;

import com.fitmart.app.entity.Product;
import com.fitmart.app.entity.Transaction;
import com.fitmart.app.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {

    private String id;
    private Integer quantity;
    private Integer price_history;
    @NotNull(message = "User id cannot be null")
    private String user_id;
    private String product_id;
    private Integer total;

    public Transaction convert(){
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setQuantity(quantity);
        transaction.setPrice_history(price_history);
        transaction.setTotal(total);

        if(user_id != null){
            User transactionUser = new User();
            transactionUser.setId(user_id);
            transaction.setUser(transactionUser);
        }

        if(product_id != null){
            Product transactionProduct = new Product();
            transactionProduct.setId(product_id);
            transaction.setProduct(transactionProduct);
        }

        return transaction;
    }
}
