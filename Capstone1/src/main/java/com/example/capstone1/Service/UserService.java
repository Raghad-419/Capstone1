package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import com.example.capstone1.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

        // Mark product as sold
        product.setSold(true);

        return "Purchase successful. Remaining balance: " + user.getBalance();
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


//Extra endpoint 3 Admin can Apply discount on product
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

//Extra Endpoint 4 User can reset password
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

//5 endpoint extra for {bounce} to check if password valid or not
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
//Extra endpoint 5 user can return product to stock
public String returnProduct(String userId,String productId,String merchantStockId){
for(User user:users){
    if(user.getId().equals(userId)){
        for(Product product: productService.getProducts()){
            if(product.getId().equals(productId)){
                if(product.getStatus().equals("Refundable")&&product.isSold()) {
                    merchantStockService.addToStock(productId, merchantStockId, 1);
                    user.setBalance(user.getBalance() + product.getPrice());
                    return"Good";
                }else return "C Product status does not allow for return.";
            }
        }return "B product not found";
    }
}return "A user not found";
}


}//end class
