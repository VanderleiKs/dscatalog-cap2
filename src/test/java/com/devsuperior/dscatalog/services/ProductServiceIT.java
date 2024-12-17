package com.devsuperior.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

    private Long existingId;
    private Long nonExistingId;
    private Long countProductDB;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 0L;
        countProductDB = 25L;
    }

    @Test
    public void deleteShouldDeleteProductWhenExistsId() {
        productService.delete(existingId);

        assertEquals(countProductDB - 1, productRepository.count());
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {

        assertThrows(ResourceNotFoundException.class, () -> productService.delete(nonExistingId));
    }

    @Test
    public void findAllPagedShouldReturnPageProductWhenPage0AndSize10() {
        var pageable = PageRequest.of(0, 10);
        var result = productService.findAllPaged(pageable);

        assertFalse(result.isEmpty());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getNumber());
        assertEquals(countProductDB, result.getTotalElements());
    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
        var pageable = PageRequest.of(10, 10);
        var result = productService.findAllPaged(pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortByName() {
        var pageable = PageRequest.of(0, 10, Sort.by("name"));
        var result = productService.findAllPaged(pageable);

        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }
}
