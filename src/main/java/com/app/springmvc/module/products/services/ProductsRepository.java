package com.app.springmvc.module.products.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springmvc.module.products.model.Product;

/**
 *
 * @author SilentNoise
 */
public interface ProductsRepository extends JpaRepository<Product, Long>{
    
}
