package com.example.capstone1.Service;

import com.example.capstone1.Model.Category;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductService {
   private final CategoryService categoryService ;
    ArrayList<Product> products =new ArrayList<>();
    ArrayList<String> reviews =new ArrayList<>();


    public ArrayList<Product> getProducts(){
        return products ;
    }

    public boolean addProduct(Product product){
        for(Category c:categoryService.getCategories()){
            if(product.getCategoryId().equals(c.getId())){
                products.add(product);
                return true;
            }
        }
        return false;
    }

    public boolean updatePoduct(String id,Product product){
        for(Product p : products){
            if(p.getId().equals(id)){
                products.set(products.indexOf(p),product);
                return true;
            }
        }
        return false;
    }

    public boolean deleteProduct(String id){
        for(Product p: products){
            if(p.getId().equals(id)){
                products.remove(products.indexOf(p));
                return true;
            }
        }
        return false;
    }
//Extra endpoint 1 User can add review on the product
    public String addReview(String productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            return "invalid rating";
        }
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                String review = String.format("Product: %s | Rating: %d | Comment: %s", productId, rating, comment);
                reviews.add(review);
                return "true";

            }
        }return "product id not found";
    }

    //1 extra endpoint for {bounce}
    public ArrayList<String> getReviews(){
        return reviews;
    }


    //Extra endpoint 2 Calculate Average rating on tha product
    public double CalculateAvgRating(String productId){
        int totalRating=0;
        int count=0;

        for(String review: reviews){
            if(review.contains("Product: "+productId)){
                String [] review_parts =review.split("\\|");
                int rating =Integer.parseInt(review_parts[1].trim().split(": ")[1]);
                totalRating=totalRating+rating;
                count++;
            }
        }
        if(count==0.0){
            return 0.0;
        }
        return (double) totalRating/count;

    }




}
