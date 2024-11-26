package com.example.capstone1.Service;

import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    ArrayList<User> users =new ArrayList<>();
    private final ProductService productService;
    private final MerchantStockService merchantStockService;
    public ArrayList<User> getUsers(){
        return users;
    }

    public void addUser(User user){
        users.add(user);
    }

    public boolean updateUser(String id ,User user){
        for(User u: users){
            if(u.getId().equals(id)){
                users.set(users.indexOf(u),user);
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String id){
        for(User u: users){
            if(u.getId().equals(id)){
                users.remove(users.indexOf(u));
                return true;
            }
        }
        return false;
    }


    public String buying(String userId, String productId, String merchantId) {
        User user = findUserById(userId);
        if (user == null) {
            return "A User not found";
        }

        Product product = findProductById(productId);
        if (product == null) {
            return "B Product not found";
        }

        MerchantStock merchantStock = findMerchantStock(productId, merchantId);
        if (merchantStock == null) {
            return "C Merchant or product stock not found";
        }
        if (merchantStock.getStock() <= 0) {
            return "D Merchant stock is insufficient";
        }

        double price = product.getPrice();
        if (user.getBalance() < price) {
            return "E User balance is insufficient";
        }

        // Deduct stock and balance
        merchantStock.setStock(merchantStock.getStock() - 1);
        user.setBalance(user.getBalance() - price);
        product.setCountOfSell(product.getCountOfSell()+1);
        if(user.getOrders()==null){
            user.setOrders(new ArrayList<>());
        }
        user.getOrders().add(product);



        return "Purchase successful. Remaining balance: " + user.getBalance();
    }


    public ArrayList<Product> getOrders(String userId){
        for(User u:users){
            if(u.getId().equals(userId)){
                if(u.getOrders()==null){
                    u.setOrders(new ArrayList<>());
                }
               return u.getOrders();
            }
        }
        return null;
    }

//2 endpoint extra for {bounce}
    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
//3 endpoint extra for {bounce}

    private Product findProductById(String productId) {
        for (Product product : productService.getProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
//4 endpoint extra for {bounce}
    private MerchantStock findMerchantStock(String productId, String merchantId) {
        for (MerchantStock stock : merchantStockService.getMerchantStocks()) {
            if (stock.getProductId().equals(productId) && stock.getMerchantId().equals(merchantId)) {
                return stock;
            }
        }
        return null;
    }


//Extra endpoint 1 Admin can Apply discount on product
public String ApplyDiscount(String adminId,String productId,double percent){
    for(User user: users){
        if(user.getId().equals(adminId)){
            if(user.getRole().equals("Admin")){
                for(Product p: productService.getProducts()){
                    if(p.getId().equals(productId)){
                       double newPrice = p.getPrice()*(1-percent/100);
                       return "Discount applied successfully, new Price: "+newPrice;
                    }
                }return "C product not found";
            }return "A not admin";
        }
    }return "B User not found";



}

//Extra Endpoint {bounce}
public String resetPassword(String userId, String oldPass ,String newPass){
        for(User user : users){
            if(user.getId().equals(userId)){
                if(user.getPassword().equals(oldPass)){
                    if(isValidPassword(newPass)){
                    user.setPassword(newPass);
                    return "G good";}
                    else return "C New password is not valid";
                }return "B User old password doesn't matches the user's current password.";
            }
        }return "A user not found";
}

// endpoint extra for {bounce} to check if password valid or not
public boolean isValidPassword(String password){
        if(password==null ||password.isEmpty()){
            return false;
        }
        if(password.length()<7){
            return false;
        }
        boolean hasDigit =false;
        boolean hasLetter=false;
        for(char c: password.toCharArray()){
            if(Character.isLetter(c)){
                hasLetter= true;
            }
            if(Character.isDigit(c)){
                hasDigit=true;
            }

        }
        if(hasDigit&&hasLetter){
            return true;
        }
        return false;
}
//2 Extra endpoint user can return product to stock
    public String returnProduct(String userId, String productId, String merchantStockId) {
        // Find the user
        User user = findUserById(userId);
        if (user == null) {
            return "A User not found";
        }

        // Check if the user has purchased the product
        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }
        boolean hasPurchased = user.getOrders().stream()
                .anyMatch(p -> p.getId().equals(productId));
        if (!hasPurchased) {
            return "H User has not purchased this product";
        }

        // Find the product
        Product product = findProductById(productId);
        if (product == null) {
            return "B Product not found";
        }

        // Check if the product is refundable
        if (!product.getStatus().equals("Refundable")) {
            return "C Product status does not allow for return";
        }

        // Return the product to stock
        merchantStockService.addToStock(productId, merchantStockId, 1);

        // Refund the user's balance
        user.setBalance(user.getBalance() + product.getPrice());

        // Remove the product from the user's orders
        user.getOrders().removeIf(p -> p.getId().equals(productId));

        return "Good Product returned successfully";
    }


//3 Extra endpoint user can add review after purchased the product
    public String  addReview(String productId,String userId, int reviewRating, String comment) {
        // Validate input
        if (reviewRating < 0 || reviewRating > 5 || comment == null || comment.trim().isEmpty()) {
            return "A Invalid rating or comment";
        }

        // Find the user
        User user = findUserById(userId);
        if (user == null) {
            return "B User not found";
        }

        if (user.getOrders() == null) {
            user.setOrders(new ArrayList<>());
        }
        boolean hasPurchased = user.getOrders().stream()
                .anyMatch(p -> p.getId().equals(productId));
        if (!hasPurchased) {
            return "H User has not purchased this product";
        }

        for (Product p : productService.getProducts()) {
            if (p.getId().equals(productId)) {
                // Initialize reviews list if null
                if (p.getReviews() == null) {
                    p.setReviews(new ArrayList<>());
                }

                // Add the review to the list
                String review = "Rating: " + reviewRating + ", Comment: " + comment;
                p.getReviews().add(review);

                // Recalculate the average rating
                int totalReviews = p.getReviews().size();
                double totalRating = p.getRating() * (totalReviews - 1) + reviewRating;
                p.setRating(totalRating / totalReviews);

                return "G";
            }
        }
        return "C Product not found"; // Product not found
    }


    // extra endpoint for {bounce}
    public ArrayList<String> getReviews(String productId){
        for(Product p: productService.getProducts()){
            if(p.getId().equals(productId)){
                if(p.getReviews()==null) {
                    p.setReviews(new ArrayList<>());
                }
                return p.getReviews();
            }
        }
        return null;
    }


    // 4 Extra endpoint to Calculate Average rating on tha product
    public double calculateAvgRating(String productId) {
        for (Product p : productService.getProducts()) {
            if (p.getId().equals(productId)) {
                return p.getRating(); // Return the already maintained average rating
            }
        }
        return 0.0; // Product not found
    }

//6 Extra endpoint that displays products that may appeal to the user based on their past orders.
    public ArrayList<Product> RecommendProducts(String userId){
        User user =findUserById(userId);
        if(user==null){
            return null;
        }
        ArrayList<Product> userOrders = user.getOrders();
        if(userOrders==null||userOrders.isEmpty()){
            return null;
        }
        ArrayList<String> orderedCategory =new ArrayList<>();
        for(Product product : userOrders){
            orderedCategory.add(product.getCategoryId());
        }

        ArrayList<Product> recommendProducts = new ArrayList<>();
        for(Product product:productService.getProducts()){
            if(orderedCategory.contains(product.getCategoryId())&& !(userOrders.contains(product))){
                recommendProducts.add(product);
            }
        }
        return recommendProducts;

    }




}//end class
