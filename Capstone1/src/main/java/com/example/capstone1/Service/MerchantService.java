package com.example.capstone1.Service;

import com.example.capstone1.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {
    ArrayList<Merchant> merchants =new ArrayList<>();

    public ArrayList<Merchant> getMerchant(){
        return merchants;
    }

    public void addMerchant(Merchant merchant){
        merchants.add(merchant);
    }

    public boolean updateMerchant(String id ,Merchant merchant){
        for(Merchant merchant1:merchants){
            if(merchant1.getId().equals(id)){
                merchants.set(merchants.indexOf(merchant1),merchant);
                return true;
            }
        }
        return false;
    }

    public boolean deleteMerchant(String id){
        for(Merchant m: merchants){
            if(m.getId().equals(id)){
                merchants.remove(merchants.indexOf(m));
                return true;
            }
        }
        return false;
    }
}
