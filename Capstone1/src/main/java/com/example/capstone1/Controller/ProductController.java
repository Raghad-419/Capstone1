package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
   private final ProductService productService;

   @GetMapping("/get")
   public ResponseEntity getProducts(){
       return ResponseEntity.status(200).body(productService.getProducts());
   }

   @PostMapping("/add")
   public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors){
       if(errors.hasErrors()){
           return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
       }
      if(productService.addProduct(product)){
       return ResponseEntity.status(200).body(new ApiResponse("Product added"));}
      return ResponseEntity.status(400).body(new ApiResponse("Category ID not found"));
   }

   @PutMapping("/update/{id}")
   public ResponseEntity updateProduct(@PathVariable String id , @RequestBody Product product, Errors errors){
       if(errors.hasErrors()){
           return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
       }
       if(productService.updatePoduct(id,product)){
           return ResponseEntity.status(200).body(new ApiResponse("Product updated"));
       }
       return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
   }

   @DeleteMapping("/delete/{id}")
   public ResponseEntity deleteProduct(@PathVariable String id){
       if(productService.deleteProduct(id)){
           return ResponseEntity.status(200).body(new ApiResponse("Product deleted"));
       }
       return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
   }

    //5 Extra Method to get bestseller products based on number of sell
    @GetMapping("/bestSeller/{numberOfSell}")
public ResponseEntity bestSeller(@PathVariable int numberOfSell){
       if(productService.bestSeller(numberOfSell)==null){
           return ResponseEntity.status(400).body(new ApiResponse("There are no products purchased yet."));
       }
       return ResponseEntity.status(200).body(productService.bestSeller(numberOfSell));
}

}//end class
