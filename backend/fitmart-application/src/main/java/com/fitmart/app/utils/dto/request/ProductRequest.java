package com.fitmart.app.utils.dto.request;

import com.fitmart.app.entity.Product;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "Name product cannot be blank")
    private String name;

    @NotNull(message = "Price cannot be null")
    private Integer price;

    public Product convert(){
        return Product.builder()
                .name(name)
                .price(price)
                .build();
    }

}
