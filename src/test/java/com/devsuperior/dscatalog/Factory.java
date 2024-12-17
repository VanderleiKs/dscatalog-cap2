package com.devsuperior.dscatalog;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

    public static Product newProduct() {
        var product = new Product(
                null,
                "Produto teste",
                "descrição do produto teste",
                50.20,
                "http://pingimage.com/p",
                Instant.now());
        product.getCategories().add(newCategory());
        return product;
    }

    public static ProductDTO newProductDTO() {
        var dto =  new ProductDTO(newProduct());
        dto.setId(1L);
        return dto;
    }

    public static Category newCategory() {
        return new Category(1L, "categoria teste");
    }
}
