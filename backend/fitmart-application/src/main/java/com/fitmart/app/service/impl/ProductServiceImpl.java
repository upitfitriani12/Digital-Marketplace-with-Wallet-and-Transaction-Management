package com.fitmart.app.service.impl;

import com.fitmart.app.entity.Product;
import com.fitmart.app.repository.ProductRepository;
import com.fitmart.app.service.ProductService;
import com.fitmart.app.utils.GeneralSpecification;
import com.fitmart.app.utils.dto.request.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;

    @Override
    public Product create(ProductRequest request) {
        return productRepository.saveAndFlush(request.convert());
    }

    @Override
    public Page<Product> getAll(Pageable pageable, ProductRequest request) {
        Specification<Product> specification = GeneralSpecification.getSpecification(request);
        return productRepository.findAll(specification, pageable);
    }

    @Override
    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Product with id " + id + " is not found"));
    }

    @Override
    public Product update(Product request) {
        return productRepository.save(request);
    }

    @Override
    public void delete(String id) {
        this.getById(id);
        productRepository.deleteById(id);
    }
}

