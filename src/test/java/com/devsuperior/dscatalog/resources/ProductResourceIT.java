package com.devsuperior.dscatalog.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceIT {

    private Long existId;
    private Long nonExistId;
    private Long countProducts;
    private MediaType mediaType;
    
    @Autowired
    private MockMvc mockMvc;

     @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        existId = 1L;
        nonExistId = 0L;
        mediaType = MediaType.APPLICATION_JSON;
    }

    @Test
    public void findAllPagedShouldReturnSortedPageProductWhenSortByName() throws Exception {
        var result = mockMvc.perform(get("/products?page=0&size=10&sort=name,asc").accept(MediaType.APPLICATION_JSON));

        result.andExpectAll(
            status().isOk(),
            jsonPath("$.content").exists(),
            jsonPath("$.content[0].name").value("Macbook Pro"),
            jsonPath("$.content[1].name").value("PC Gamer"),
            jsonPath("$.content[2].name").value("PC Gamer Alfa")
        );
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistsId() throws Exception {
        var product = Factory.newProductDTO();
        var result = mockMvc.perform(put("/products/{id}", existId)
                .content(objectMapper.writeValueAsString(product))
                .contentType(mediaType)
                .accept(mediaType));

        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").exists(),
                jsonPath("$.id").value(existId));
    }

    @Test
    public void updateShouldReturnNotFoundWhenDoesNotExistId() throws Exception {
        var product = Factory.newProductDTO();
        var result = mockMvc.perform(put("/products/{id}", nonExistId)
                .content(objectMapper.writeValueAsString(product))
                .contentType(mediaType)
                .accept(mediaType));

        result.andExpect(status().isNotFound());
    }
}
