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

    //5 Extra Method to get bestseller products based on number of sell
    public ArrayList<Product> bestSeller(int numberOfProduct) {
        // Create a copy of the products list
        ArrayList<Product> sortedProducts = new ArrayList<>(products);

//perform sorted array of product
        for (int i = 0; i < sortedProducts.size() - 1; i++) {
            for (int j = i + 1; j < sortedProducts.size(); j++) {
                if (sortedProducts.get(i).getCountOfSell() < sortedProducts.get(j).getCountOfSell()) {
                    // Swap the products
                    Product temp = sortedProducts.get(i);
                    sortedProducts.set(i, sortedProducts.get(j));
                    sortedProducts.set(j, temp);
                }
            }
        }

        // Prepare the result list with a limit
        ArrayList<Product> result = new ArrayList<>();
        for (int i = 0; i < Math.min(numberOfProduct, sortedProducts.size()); i++) {
            result.add(sortedProducts.get(i));
        }

        if(result.isEmpty()){
           return null;
        }
        return result;
    }



}
