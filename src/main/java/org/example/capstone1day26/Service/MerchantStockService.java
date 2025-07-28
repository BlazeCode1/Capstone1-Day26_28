package org.example.capstone1day26.Service;


import lombok.RequiredArgsConstructor;
import org.example.capstone1day26.Model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {
    private final ProductService productService;
    private final MerchantService merchantService;
    private final UserService userService;
    private final CategoryService categoryService;
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    public ArrayList<MerchantStock> getAllMerchantStocks(){
        return merchantStocks;
    }

    public int addMerchantStock(MerchantStock merchantStock){
        if(checkIsEmptyProducts()) return 0;
        if(checkIsEmptyMerchants())  return 1;
        for (MerchantStock m : merchantStocks){
            if(merchantStock.getID().equals(m.getID())){
                return 5;
            }
        }

        if(checkIfMerchantFound(merchantStock.getMerchantID())) return 2;
        if(checkIfProductFound(merchantStock.getProductID())) return 3;

        merchantStock.setStockAddedDate(LocalDate.now());
        merchantStock.setClearance(false);
        merchantStocks.add(merchantStock);

        return 4;

    }

    public int updateMerchantStock(MerchantStock merchantStock){
        if(checkIsEmptyProducts()) return 0;
        if(checkIsEmptyMerchants())  return 1;

        if(checkIfMerchantFound(merchantStock.getMerchantID())) return 2;
        if(checkIfProductFound(merchantStock.getProductID())) return 3;
        for (MerchantStock m: merchantStocks){
            if(merchantStock.getID().equals(m.getID())){
                merchantStocks.set(merchantStocks.indexOf(m),merchantStock);
                return 5;
            }
        }
        return 4;
    }

    public boolean deleteMerchantStock(String ID){
        for (MerchantStock m : merchantStocks){
            if(ID.equals(m.getID())){
                merchantStocks.remove(m);
                return true;
            }
        }
        return false;
    }

    public int addMoreStock(String productID, String merchantID,int amount){
        if(checkIsEmptyProducts()) return 0;
        if(checkIsEmptyMerchants()) return 1;
        if(checkIfProductFound(productID)) return 2;
        if(checkIfMerchantFound(merchantID)) return 3;

        for (MerchantStock m : merchantStocks){
            if(m.getProductID().equals(productID) && m.getMerchantID().equals(merchantID)){
            m.setStock(m.getStock() + amount);
            return 5;
            }
        }
        return 4;

    }

    public int userBuyProduct(String userID, String productID, String merchantID){
        if(checkIsEmptyProducts()) return 0;
        if(checkIsEmptyMerchants()) return 1;
        if(checkIfProductFound(productID)) return 2;
        if(checkIfMerchantFound(merchantID)) return 3;
        if(checkIfUserFound(userID)) return 4;
        if(checkIfProductInStock(productID,merchantID)) return 5;
        Product targetProduct= null;
        User targetUser= null;
        MerchantStock targetStock = null;
        Category targetCategory = null;
        for(Product p : productService.getAllProducts()){
            if(p.getID().equals(productID)){
                targetProduct = p;
                break;
            }
        }

        for (User u : userService.getAllUsers()){
            if(u.getID().equals(userID)){
                targetUser = u;
                break;
            }
        }

        for (MerchantStock m : merchantStocks){
            if(productID.equals(m.getProductID()) && merchantID.equals(m.getMerchantID())){
                targetStock = m;
                break;
            }
        }
        Merchant targetMerchant= null;
        for (Merchant m : merchantService.getAllMerchants()){
            if(merchantID.equals(m.getID())){
                targetMerchant = m;
                break;
            }
        }


        if(targetMerchant == null) return 3;
        if(targetMerchant.isSuspended()) return 12;




        if(targetStock == null || targetProduct == null || targetUser == null ) return 6; // to be safe

        if(targetStock.getStock() == 0) return 9;
        double finalPrice = targetStock.isClearance() ? targetStock.getClearancePrice() : targetProduct.getPrice();

        if(targetUser.getBalance() < finalPrice) return 7;
        //User deductBalance
        targetUser.setBalance(targetUser.getBalance() - finalPrice);

        //Product setters
        targetProduct.setLastSoldDate(LocalDate.now());
        targetProduct.setTotalSold(targetProduct.getTotalSold() + 1);
        targetProduct.setTotalRevenue(targetProduct.getTotalRevenue() + finalPrice);

        //Stock setters
        targetStock.setStock(targetStock.getStock() - 1);
        targetStock.setPurchaseCount(targetStock.getPurchaseCount() + 1);
        return 8;

    }

    //FIRST METHOD
    public ArrayList<Product> getOutOfStockProductsForMerchant(String merchantID){
        ArrayList<String> outOfStockIDs = new ArrayList<>();
        ArrayList<Product> outOfStockMerchants = new ArrayList<>();
        for (MerchantStock m : merchantStocks){
            if(m.getMerchantID().equals(merchantID) && m.getStock() == 0){
                outOfStockIDs.add(m.getProductID());
            }
        }
        for (Product p : productService.getAllProducts()){
            if(outOfStockIDs.contains(p.getID())){
                outOfStockMerchants.add(p);
            }
        }
        return outOfStockMerchants;
    }

    //TODO: THIRD METHOD

    public boolean triggerClearance(String merchantID){
        LocalDate today = LocalDate.now();
        LocalDate cutOff = today.minusDays(45);
        boolean anyClearanceTriggered = false;
        for (MerchantStock m : merchantStocks){
            if(merchantID.equals(m.getMerchantID())) {
                if(m.getStock() <=20 || m.getPurchaseCount() <=5) continue;


                if(m.getStockAddedDate() == null || !m.getStockAddedDate().isBefore(cutOff)) continue;

                if(m.getProductID() == null) continue;
                Product targetProduct = findProduct(m.getProductID());
                if(targetProduct == null) continue;

                m.setClearance(true);
                m.setClearancePrice(targetProduct.getPrice() * 0.7);
                anyClearanceTriggered = true;
            }



        }
        return anyClearanceTriggered;
    }

    public String evaluateCommissionTier(String merchantID){
        Merchant targetMerchant = null;
        double totalRevenue = 0;
        for (Merchant m: merchantService.getAllMerchants()){
            if(m.getID().equals(merchantID)){
                targetMerchant = m;
                break;
            }
        }

        if(targetMerchant == null) return null;

        for (MerchantStock stock : merchantStocks){
            if(stock.getMerchantID().equals(merchantID)){

                if (stock.getPurchaseCount() != 0) {
                    Product matchedProduct = null;
                    for (Product p : productService.getAllProducts()){
                        if(p.getID().equals(stock.getProductID())){
                            matchedProduct = p;
                            break;
                        }
                    }

                    if(matchedProduct != null){
                        double price = stock.isClearance() ? stock.getClearancePrice() : matchedProduct.getPrice();
                        totalRevenue += price * stock.getPurchaseCount();;
                    }
                }

            }
        }

        double commissionRate;
        if(totalRevenue > 10000){
            commissionRate = 0.05;
        }else if(totalRevenue > 5000) {
            commissionRate = 0.09;
        }else {
            commissionRate = 0.12;
        }
        targetMerchant.setCommissionRate(commissionRate);

        return "Merchant " + targetMerchant.getName()
                + " has total revenue: " + totalRevenue + " SAR and tier: "
                + (commissionRate * 100) + "% commission rate applied.";
    }


    public String evaluatingMerchantViolation(String merchantID){
        Merchant targetMerchant = null;
        for (Merchant m : merchantService.getAllMerchants()){
            if(merchantID.equals(m.getID())){
                targetMerchant = m;
                break;
            }
        }
        if(targetMerchant == null) return "Invalid Merchant ID";

        int violationCount = 0;
        for (MerchantStock ms : merchantStocks){
            if(!ms.getMerchantID().equals(merchantID)) continue;

            Product product = null;

            for (Product p : productService.getAllProducts()){
                if(ms.getProductID().equals(p.getID())){
                    product = p;
                    break;
                }
            }

            if (product == null) continue;

            ArrayList<String> matchingProductIDs = new ArrayList<>();

            for (Product p : productService.getAllProducts()){
                if(p.getProductCode().equals(product.getProductCode())){
                    matchingProductIDs.add(p.getID());
                }
            }

            int productCount = 0;
            double totalPrice = 0;

            for (MerchantStock otherStock : merchantStocks){
                if(matchingProductIDs.contains(otherStock.getProductID())){
                    Product otherProduct = null;
                    for (Product p : productService.getAllProducts()){
                        if(otherStock.getProductID().equals(p.getID())){
                            otherProduct = p;
                            break;
                        }

                    }
                    if(otherProduct != null){
                        double price = otherStock.isClearance() ? otherStock.getClearancePrice() : otherProduct.getPrice();
                        totalPrice += price;
                        productCount++;
                    }
                }
            }

            if(productCount == 0) continue;

            double avgPrice = totalPrice/productCount;
            double thisPrice = ms.isClearance() ? ms.getClearancePrice() : product.getPrice();

            if(thisPrice > 1.5 * avgPrice){
                violationCount++;
            }


        }

        if (violationCount >= 5) {
            targetMerchant.setSuspended(true);
            for (MerchantStock ms : merchantStocks){
                if(merchantID.equals(ms.getMerchantID())){
                    ms.setStock(0);
                }
            }
            return "Merchant " + targetMerchant.getName()
                    + " has been suspended due to " + violationCount + " pricing violations.";
        }

        return "Merchant " + targetMerchant.getName()
                + " has " + violationCount + " pricing violations. Not suspended.";
    }






    //Helper Methods
    public boolean checkIsEmptyProducts(){
        return productService.getAllProducts().isEmpty();
    }
    public boolean checkIsEmptyMerchants(){
        return merchantService.getAllMerchants().isEmpty();
    }

    public boolean checkIfProductFound(String ID){
        for (Product p : productService.getAllProducts()){
            if(ID.equals(p.getID()))
                return false;
        }
        return true;
    }
    public boolean checkIfMerchantFound(String ID){
        for (Merchant m : merchantService.getAllMerchants()){
            if(ID.equals(m.getID()))
                return false;
        }
        return true;
    }

    public boolean checkIfMerchantHasProductsListed(String merchantID){
        for (MerchantStock m : merchantStocks){
            if(m.getMerchantID().equals(merchantID))
                return false;
        }
        return true;
    }

    public boolean checkIfUserFound(String userID){
        for (User u: userService.getAllUsers()){
            if(u.getID().equals(userID)){
                return false;
            }
        }
        return true;
    }

    public boolean checkIfProductInStock(String productID, String merchantID){
        for (MerchantStock m : merchantStocks){
            if(m.getProductID().equals(productID) && m.getMerchantID().equals(merchantID)){
                return false;
            }
        }
        return true;
    }

    public Product findProduct(String productID){
        if(!productService.getAllProducts().isEmpty()){
            for (Product p : productService.getAllProducts()){
                if(productID.equals(p.getID()))
                    return p;
            }
        }
        return null;
    }

}
