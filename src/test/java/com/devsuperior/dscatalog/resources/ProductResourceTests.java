package com.devsuperior.dscatalog.resources;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    private Long existId;
    private Long nonExistId;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> pageDTO;
    private MediaType mediaType;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        existId = 1L;
        nonExistId = 2L;
        mediaType = MediaType.APPLICATION_JSON;
        product = Factory.newProduct();
        productDTO = Factory.newProductDTO();
        pageDTO = new PageImpl<>(List.of(productDTO));

        when(productService.findAllPaged(Pageable.unpaged())).thenReturn(pageDTO);
        when(productService.findById(existId)).thenReturn(productDTO);
        when(productService.findById(nonExistId)).thenThrow(ResourceNotFoundException.class);
        when(productService.update(eq(existId), any())).thenReturn(productDTO);
        when(productService.update(eq(nonExistId), any())).thenThrow(ResourceNotFoundException.class);
        when(productService.insert(any())).thenReturn(productDTO);

        doNothing().when(productService).delete(existId);
        doThrow(ResourceNotFoundException.class).when(productService).delete(nonExistId);
    }

    @Test
    public void findAllPagedShoudReturnPageProductDTO() throws Exception {
        var result = mockMvc.perform(get("/products").accept(mediaType));

        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenExistsId() throws Exception {
        var result = mockMvc.perform(get("/products/{id}", existId).accept(mediaType));

        result.andExpectAll(
                status().isOk(),
                content().contentType(mediaType),
                jsonPath("$.id").exists(),
                jsonPath("$.id").value(existId));
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenDoesNotExistId() throws Exception {
        var result = mockMvc.perform(get("/products/{id}", nonExistId).accept(mediaType));

        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.id").doesNotExist());
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistsId() throws Exception {
        var result = mockMvc.perform(put("/products/{id}", existId)
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(mediaType)
                .accept(mediaType));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").exists(),
                jsonPath("$.id").value(existId));
    }

    @Test
    public void updateShouldReturnNotFoundWhenDoesNotExistId() throws Exception {
        var result = mockMvc.perform(put("/products/{id}", nonExistId)
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(mediaType)
                .accept(mediaType));

        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.id").doesNotExist());
    }

    @Test
    public void insertShouldReturnProductDTO() throws Exception {
        var result = mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productDTO))
                .contentType(mediaType)
                .accept(mediaType));

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").exists());
    }

    @Test
    public void deleteShouldReturnNoContentWhenExistsId() throws Exception {
        var result = mockMvc.perform(delete("/products/{id}", existId).accept(mediaType));

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenDoesNotExistId() throws Exception {
        var result = mockMvc.perform(delete("/products/{id}", nonExistId).accept(mediaType));

        result.andExpect(status().isNotFound());
    }
}
