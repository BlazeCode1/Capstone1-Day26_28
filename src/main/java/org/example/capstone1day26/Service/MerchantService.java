package org.example.capstone1day26.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Model.Merchant;
import org.example.capstone1day26.Model.MerchantStock;
import org.example.capstone1day26.Model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantService {
    ArrayList<Merchant> merchants = new ArrayList<>();
    private final ProductService productService;
    public ArrayList<Merchant> getAllMerchants() {
        return merchants;
    }

    public boolean addMerchant(Merchant merchant) {
        for (Merchant m : merchants) {
            if (m.getID().equals(merchant.getID())) {
                return false;// merchant ID Used
            }
        }
        merchants.add(merchant);
        return true;//  Added Successfully
    }


    public boolean updateMerchant(Merchant merchant) {
        for (Merchant m : merchants) {
            if (m.getID().equals(merchant.getID())) {
                merchants.set(merchants.indexOf(m), merchant);
                return true; //updated successfully
            }
        }
        return false;// Merchant ID not found
    }

    public boolean deleteMerchant(String ID) {
        for (Merchant m : merchants) {
            if (ID.equals(m.getID())) {
                merchants.remove(m);
                return true;
            }
        }
        return false;
    }






}
