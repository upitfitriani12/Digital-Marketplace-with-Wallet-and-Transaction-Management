package com.fitmart.app.utils.dto.response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fitmart.app.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class TransactionResponse {


    private String id;
    private Integer quantity;
    private Integer price_history;
    private String email;
    private String product_name;
    private Date date_start;
    private Date date_end;
    private Integer duration;
    private Integer total;


    public static TransactionResponse fromTransaction(Transaction transaction){
        String user_id = (transaction.getUser() != null) ? transaction.getUser().getId() : null;
        String product_id = (transaction.getProduct() != null) ? transaction.getProduct().getId() : null;
        return TransactionResponse.builder()
                .id(transaction.getId())
                .quantity(transaction.getQuantity())
                .email(transaction.getUser().getEmail())
                .product_name(transaction.getProduct().getName())
                .price_history(transaction.getPrice_history())
                .date_start(transaction.getDate_start())
                .date_end(transaction.getDate_end())
                .duration(transaction.getDuration())
                .total(transaction.getTotal())
                .build();
    }

    public static  Page<TransactionResponse> convertToUserResponsePage(Page<Transaction> userPage) {
        List<TransactionResponse> userResponses = userPage.getContent().stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());

        return new PageImpl<>(userResponses, userPage.getPageable(), userPage.getTotalElements());
    }

    public static  List<TransactionResponse> confertListTransaction(List<Transaction> userPage) {
        return  userPage.stream()
                .map(TransactionResponse::fromTransaction)
                .collect(Collectors.toList());


    }
}

