package com.fitmart.app.utils.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.fitmart.app.entity.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class WalletResponse {

    private String id;
    private Integer balance;
    private String email;

    public static WalletResponse fromWallet(Wallet wallet){
        String user_id = (wallet.getUser() != null) ? wallet.getUser().getId() : null;

        return WalletResponse.builder()
                .id(wallet.getId())
                .balance(wallet.getBalance())
                .email(wallet.getUser().getEmail())
                .build();
    }


    public static  Page<WalletResponse> convertToUserResponsePage(Page<Wallet> userPage) {
        List<WalletResponse> userResponses = userPage.getContent().stream()
                .map(WalletResponse::fromWallet)
                .collect(Collectors.toList());

        return new PageImpl<>(userResponses, userPage.getPageable(), userPage.getTotalElements());
    }




}
