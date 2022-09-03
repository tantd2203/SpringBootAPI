package com.example.stringbot.repostories;

import com.example.stringbot.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Products ,Long> {
    List<Products> findByProductName (String productName);
}
