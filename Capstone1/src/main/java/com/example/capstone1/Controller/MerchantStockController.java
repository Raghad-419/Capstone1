package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Service.MerchantStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchantStock")
@RequiredArgsConstructor
public class MerchantStockController {
  private final MerchantStockService merchantStockService ;


  @GetMapping("/get")
  public ResponseEntity getMerchantStock(){
      return ResponseEntity.status(200).body(merchantStockService.getMerchantStocks());
  }


  @PostMapping("/add")
  public ResponseEntity addMerchantStock(@RequestBody @Valid MerchantStock merchantStock, Errors errors){
      if(errors.hasErrors()){
          return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
      }
      if(merchantStockService.addMerchantStock(merchantStock).equals("Merchant Stock added")){
          return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock added"));
      }
      if(merchantStockService.addMerchantStock(merchantStock).equals("Product id not found")){
          return ResponseEntity.status(400).body(new ApiResponse("Product id not found"));
      }
          return ResponseEntity.status(400).body(new ApiResponse("Merchant ID not found"));


  }

  @PutMapping("/update/{id}")
  public ResponseEntity updateMerchantStock(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock ,Errors errors){
      if(errors.hasErrors()){
          return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
      }
      if(merchantStockService.updateMerchantStock(id,merchantStock)){
          return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock updated"));
      }
      return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity deleteMerchantStock(@PathVariable String id){
      if(merchantStockService.deleteMerchantStock(id)){
          return ResponseEntity.status(200).body(new ApiResponse("Merchant Stock deleted"));
      }
      return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
  }


  @PutMapping("/addToStock/{product_id}/{merchant_id}/{amount}")
 public ResponseEntity addToStock(@PathVariable String product_id ,@PathVariable String merchant_id,@PathVariable int amount){
      if(merchantStockService.addToStock(product_id,merchant_id,amount)){
          return ResponseEntity.status(200).body(new ApiResponse("More stocks of product have been added to a merchant Stock"));
      }
      return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
 }




}
