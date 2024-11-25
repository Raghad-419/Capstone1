package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import com.example.capstone1.Model.MerchantStock;
import com.example.capstone1.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {
    private final ProductService productService;
    private final MerchantService merchantService;
    ArrayList<MerchantStock> merchantStocks=new ArrayList<>();

    public ArrayList<MerchantStock> getMerchantStocks(){
        return merchantStocks;
    }

    public String addMerchantStock(MerchantStock merchantStock){
        for(Merchant m:merchantService.getMerchant()){
            if(m.getId().equals(merchantStock.getMerchantId())){
                for (Product p:productService.getProducts()) {
                    if (p.getId().equals(merchantStock.getProductId())) {
                        merchantStocks.add(merchantStock);
                        return "Merchant Stock added";
                    }
                }return "Product id not found";
            }
        }return "Merchant ID not found";
    }

    public boolean updateMerchantStock(String id ,MerchantStock merchantStock){
        for(MerchantStock m:merchantStocks) {
            if(m.getId().equals(id)){
               merchantStocks.set(merchantStocks.indexOf(m),merchantStock);
               return true;
            }
        }
        return false;
    }

    public boolean deleteMerchantStock(String id){
        for (MerchantStock m :merchantStocks){
            if(m.getId().equals(id)){
                merchantStocks.remove(merchantStocks.indexOf(m));
                return true;
            }
        }
        return false;
    }

    public boolean addToStock(String  product_id ,String merchant_id, int amount){
        for (MerchantStock m: merchantStocks){
            if(m.getMerchantId().equals(merchant_id)){
               if(m.getProductId().equals(product_id)){
                   m.setStock(m.getStock()+amount);
                   return true;
               }
            }
        }
        return false;
    }

}//end of class
