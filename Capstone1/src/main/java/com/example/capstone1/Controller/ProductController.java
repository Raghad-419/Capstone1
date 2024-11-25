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

    //Extra endpoint 1 User can add review on the product
   @PostMapping("/addReview/{productId}/{rating}")
public ResponseEntity addReview(@PathVariable String productId, @PathVariable int rating,@RequestBody String comment) {
    String result = productService.addReview(productId, rating, comment);
    if (result.equals("true")) {
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully."));
    }
    if (result.equals("invalid rating")) {
        return ResponseEntity.status(400).body(new ApiResponse("invalid rating"));
    }if(result.equals("User not found")){
        return ResponseEntity.status(400).body(new ApiResponse("User not found"));
       }
    return ResponseEntity.status(400).body(new ApiResponse("product id not found"));

}
    //1 extra endpoint for {bounce}
@GetMapping("/getReview")
public ResponseEntity getReview(){
       return ResponseEntity.status(200).body(productService.getReviews());
}

    //Extra endpoint 2 Calculate Average rating on tha product
@GetMapping("/CalculateAvgRating/{productId}")
public ResponseEntity CalculateAvgRating(@PathVariable String productId){
       if(productService.CalculateAvgRating(productId)==0){
           return ResponseEntity.status(400).body(new ApiResponse("Review not found"));
       }
       return ResponseEntity.status(200).body(new ApiResponse("Average rating= "+productService.CalculateAvgRating(productId)));

}



}//end class
