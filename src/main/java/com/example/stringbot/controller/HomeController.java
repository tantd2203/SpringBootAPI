package com.example.stringbot.controller;

import com.example.stringbot.models.Products;
import com.example.stringbot.models.ResponseObject;
import com.example.stringbot.repostories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/v1/Products")
public class HomeController {


    //DI = Dependency Injection
    @Autowired
    private ProductRepository repository;

    @GetMapping("/getProducts")
    List<Products> getAllProducts() {
        return repository.findAll();
        // You must save this to Database new we have H2Database
        // use can also spend request using Postman
    }

    // can is null if not data
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Products> foundProduct = repository.findById(id);

        return foundProduct.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Ok", "Query Success", foundProduct)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("false", "cant not find", foundProduct));
        //  xửa lý toán tử 3 ngôi
    }

    //insert
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> ínert(@RequestBody Products newProduct) {
        // trim bor qua String cuối và đầu
        List<Products> foundProductName = repository.findByProductName(newProduct.getProductName().trim());
        return foundProductName.size() > 0 ? ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("false", "dulicate name", foundProductName)
        ) : ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "insert Success", repository.save(newProduct))
        );

    }
}
