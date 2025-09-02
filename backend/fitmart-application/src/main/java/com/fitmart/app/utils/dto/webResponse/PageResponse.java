package com.fitmart.app.utils.dto.webResponse;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> content;
    private Long total_elements;
    private Integer total_pages;
    private Integer page;
    private Integer size;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.total_elements = page.getTotalElements();
        this.total_pages = page.getTotalPages();
        this.page = page.getNumber();
        this.size = page.getSize();
    }
}