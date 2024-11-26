package com.example.capstone1.Controller;

import com.example.capstone1.ApiResponse.ApiResponse;
import com.example.capstone1.Model.User;
import com.example.capstone1.Service.UserService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
 private final UserService userService;

 @GetMapping("/get")
 public ResponseEntity getUsers(){
     return ResponseEntity.status(200).body(userService.getUsers());
 }

 @PostMapping("/add")
 public ResponseEntity addUser(@RequestBody @Valid User user, Errors errors){
     if(errors.hasErrors()){
         return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
     }
     userService.addUser(user);
     return ResponseEntity.status(200).body(new ApiResponse("User added"));
 }

 @PutMapping("/update/{id}")
 public ResponseEntity updateUser(@PathVariable String id, @RequestBody @Valid User user ,Errors errors){
     if(errors.hasErrors()){
         return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
     }
     if(userService.updateUser(id,user)){
         return ResponseEntity.status(200).body(new ApiResponse("User updated"));
     }
     return ResponseEntity.status(400).body(new ApiResponse("ID not found"));
 }

 @DeleteMapping("/delete/{id}")
 public ResponseEntity deleteUser(@PathVariable String id){
     if(userService.deleteUser(id)){
         return ResponseEntity.status(200).body(new ApiResponse("User deleted"));
     }
     return ResponseEntity.status(400).body(new ApiResponse("ID not found"));

 }



 @PutMapping("/buying/{user_id}/{product_id}/{merchant_id}")
 public ResponseEntity buying(@PathVariable String user_id,@PathVariable String product_id,@PathVariable String merchant_id){
     char result= userService.buying(user_id,product_id,merchant_id).charAt(0);
     return switch (result) {
         case 'A' -> ResponseEntity.status(400).body(new ApiResponse("User not found"));
         case 'B' -> ResponseEntity.status(400).body(new ApiResponse("Product not found"));
         case 'C' -> ResponseEntity.status(400).body(new ApiResponse("Merchant or product stock not found"));
         case 'D' -> ResponseEntity.status(400).body(new ApiResponse("Merchant stock is insufficient"));
         case 'E' -> ResponseEntity.status(400).body(new ApiResponse("User balance is insufficient"));
         default -> ResponseEntity.status(200).body(new ApiResponse("Purchase successful."));
     };

 }

 @GetMapping("/getOrders/{userId}")
 public ResponseEntity getOrders(@PathVariable String userId){
     if(userService.getOrders(userId)==null){
         return ResponseEntity.status(400).body(new ApiResponse("User id not found"));
     }
     return ResponseEntity.status(200).body(userService.getOrders(userId));
 }

    //Extra endpoint 1 Admin can Apply discount on product
 @GetMapping("/ApplyDiscount/{adminId}/{productId}/{percent}")
 public ResponseEntity ApplyDiscount(@PathVariable String adminId,@PathVariable String productId,@PathVariable double percent){
     char result = userService.ApplyDiscount(adminId,productId,percent).charAt(0);
     return switch (result){
         case 'A' -> ResponseEntity.status(400).body(new ApiResponse("You are not authorized to apply the discount."));
         case 'B' -> ResponseEntity.status(400).body(new ApiResponse("User not found"));
         case 'C' -> ResponseEntity.status(400).body(new ApiResponse("product not found"));
         default -> ResponseEntity.status(200).body(new ApiResponse(userService.ApplyDiscount(adminId,productId,percent)));

     };

 }


    //Extra Endpoint {bounce} User can reset password
    @PutMapping("/resetPassword/{userId}/{oldPass}/{newPass}")
    public ResponseEntity resetPassword(@PathVariable String userId,@PathVariable String oldPass ,@PathVariable String newPass){
     char result =userService.resetPassword(userId,oldPass,newPass).charAt(0);
     return switch (result){
         case 'A' -> ResponseEntity.status(400).body(new ApiResponse("user not found"));
         case 'B' ->ResponseEntity.status(400).body(new ApiResponse("User old password doesn't matches the user's current password."));
         case 'C' ->ResponseEntity.status(400).body(new ApiResponse("New password is not valid"));
         default -> ResponseEntity.status(200).body(new ApiResponse("Password Change"));
     };
    }

    //2 Extra endpoint user can return product to stock
    @PutMapping("/returnProduct/{userId}/{productId}/{merchantStockId}")
    public ResponseEntity returnProduct(@PathVariable String userId,@PathVariable String productId,@PathVariable String merchantStockId){
     char result =userService.returnProduct(userId,productId,merchantStockId).charAt(0);
        return switch (result){
            case 'A' -> ResponseEntity.status(400).body(new ApiResponse("user not found"));
            case 'B' ->ResponseEntity.status(400).body(new ApiResponse("Product not found"));
            case 'C' ->ResponseEntity.status(400).body(new ApiResponse("Product status does not allow for return."));
            case 'H' -> ResponseEntity.status(400).body(new ApiResponse("User has not purchased this product"));
            default -> ResponseEntity.status(200).body(new ApiResponse("Returned successfully"));
        };
    }



    //3 Extra endpoint user can add review after purchased the product
    @PostMapping("/addReview/{productId}/{userId}/{rating}")
    public ResponseEntity addReview(@PathVariable String productId,@PathVariable String userId ,@PathVariable int rating,@RequestBody String comment) {
        char result =userService.addReview(productId,userId,rating,comment).charAt(0);
        return switch (result){
            case 'A' -> ResponseEntity.status(400).body(new ApiResponse("Invalid rating or comment"));
            case 'B' ->ResponseEntity.status(400).body(new ApiResponse("User not found"));
            case 'H' ->ResponseEntity.status(400).body(new ApiResponse("User has not purchased this product"));
            case 'G' ->ResponseEntity.status(200).body(new ApiResponse("Review Added successfully")) ;
            default -> ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        };


    }
    // extra endpoint for {bounce}
    @GetMapping("/getReview/{productId}")
    public ResponseEntity getReviews(@PathVariable String productId){
        if(userService.getReviews(productId)==null){
            return ResponseEntity.status(400).body(new ApiResponse("product ID not found"));
        }
        return ResponseEntity.status(200).body(userService.getReviews(productId));
    }

    // 4 Extra endpoint to Calculate Average rating on tha product
    @GetMapping("/CalculateAvgRating/{productId}")
    public ResponseEntity CalculateAvgRating(@PathVariable String productId){
        if(userService.calculateAvgRating(productId)==0){
            return ResponseEntity.status(400).body(new ApiResponse("Review not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Average rating= "+userService.calculateAvgRating(productId)));

    }
//6 Extra endpoint that displays products that may appeal to the user based on their past orders.
    @GetMapping("/RecommendProducts/{userId}")
    public ResponseEntity RecommendProducts(@PathVariable String userId){
     if(userService.RecommendProducts(userId)==null){
         return ResponseEntity.status(400).body(new ApiResponse("User orders not found"));
     }
     return ResponseEntity.status(200).body(userService.RecommendProducts(userId));
    }


}//end class
