package com.fitmart.app.utils.dto.request;
import com.fitmart.app.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    public Category convert() {
        return Category.builder()
                .name(name)
                .build();
    }
}
