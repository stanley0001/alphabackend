package com.example.demo.persistence.repository;

import com.example.demo.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Products, Long> {


    Optional<Products> getByCode(String code);

    Products findByCode(String productCode);
}
