package com.fitmart.app.utils.dto.response;

import com.fitmart.app.entity.Product;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {

    private String id;
    private String name;
    private Integer price;
    private List<CategoryResponse> categories;

    public static ProductResponse fromProduct(Product product){
        List<CategoryResponse> categoryResponses = product.getCategories() == null ?
                List.of() :
                product.getCategories().stream()
                        .map(CategoryResponse::fromCategory)
                        .collect(Collectors.toList());
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .categories(categoryResponses)
                .build();
    }
}