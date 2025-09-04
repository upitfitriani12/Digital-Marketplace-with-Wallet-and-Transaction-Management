package com.fitmart.app.utils.dto.request;

import com.fitmart.app.entity.User;
import com.fitmart.app.entity.Wallet;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletRequest {

    private String id;
    private String userId;

    @NotNull(message = "Balance cannot be null")
    private Integer balance;

    public Wallet convert() {
        Wallet wallet = new Wallet();
        wallet.setId(this.id);
        wallet.setBalance(this.balance);
        return wallet;
    }
}

