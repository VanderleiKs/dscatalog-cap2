package com.devsuperior.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private Long existId;
    private Long nonExistId;
    private Long dependentId;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl<Product> page;
    private Category category;

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        existId = 1L;
        nonExistId = 1000L;
        dependentId = 3L;
        product = Factory.newProduct();
        productDTO = Factory.newProductDTO();
        category = Factory.newCategory();
        page = new PageImpl<>(List.of(product));

        Mockito.when(categoryRepository.getReferenceById(existId)).thenReturn(category);

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(repository.existsById(existId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);
        Mockito.when(repository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(page);
        Mockito.when(repository.findById(existId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistId)).thenReturn(Optional.empty());
        Mockito.when(repository.getReferenceById(existId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistId)).thenThrow(EntityNotFoundException.class);

        Mockito.doNothing().when(repository).deleteById(existId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> service.delete(existId));

        Mockito.verify(repository, Mockito.times(1)).deleteById(existId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistId);
        });
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        var pageable = PageRequest.of(0, 20);
        var result = service.findAllPaged(pageable);

        assertNotNull(result);
        verify(repository).findAll(pageable);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenExistId() {
        var result = service.findById(existId);

        assertNotNull(result);
        verify(repository).findById(existId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {

        assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistId));
        verify(repository).findById(nonExistId);
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistsId() {
        var result = service.update(existId, productDTO);

        assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenDoesNotExistId() {

        assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistId, productDTO));
    }
}
