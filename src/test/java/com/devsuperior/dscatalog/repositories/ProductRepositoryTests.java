package com.devsuperior.dscatalog.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.entities.Product;

@DataJpaTest
public class ProductRepositoryTests {

    private Integer countIdProduct;
    private Long existId;
    private Long nonExistId;

    @Autowired
    private ProductRepository productRepository;
    
    @BeforeEach
    void setup() {
        countIdProduct = 25;
        existId = 1L;
        nonExistId = 0L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(existId);

        Optional<Product> result = productRepository.findById(existId);

        assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldAddNewProductWhenIdIsNull() {
       var newProduct =  Factory.newProduct();
       var productSave = productRepository.save(newProduct);

       assertNotNull(productSave);
       assertNotNull(productSave.getId());
       assertEquals(countIdProduct+1, productSave.getId());
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalWhenIdExist() {
        Optional<Product> product = productRepository.findById(existId);

        assertNotEquals(Optional.empty(), product);
        assertTrue(product.isPresent());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalWhenIdDoesNotExist() {
        Optional<Product> product = productRepository.findById(nonExistId);

        assertEquals(Optional.empty(), product);
    }

}
