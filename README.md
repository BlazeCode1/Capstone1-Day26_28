#  Advanced Endpoints – Amazon Clone Backend

This document describes **5 business-driven methods** added to the backend system.

---

## 1. `outOfStockMerchants(String merchantID)`

**Service**: `MerchantStockService`  
Returns all products sold by a given merchant that are currently out of stock.

---

## 2. `triggerClearance(String merchantID)`

**Service**: `MerchantStockService`  
Triggers clearance pricing for qualifying merchant products if:
- Stock is older than 45 days
- Stock > 20
- Purchase count > 5  
  Applies a 30% discount on the product price.

---

## 3. `evaluateCommissionTier(String merchantID)`

**Service**: `MerchantStockService`  
Calculates total revenue for a merchant and assigns a commission tier:

| Revenue Range        | Commission Rate |
|----------------------|------------------|
| > 10,000             | 5%               |
| 5,000 – 10,000       | 9%               |
| ≤ 5,000              | 12%              |

---

## 4. `evaluatingMerchantViolation(String merchantID)`

**Service**: `MerchantStockService`  
Detects if a merchant is pricing their products more than **1.5× the average** market price (by `productCode`).  
If `violationCount ≥ 5`, the merchant is:
- Marked as suspended
- All their stock is set to 0

---

## 5. `topFiveProductsPurchased(String start, String end, int userLimit)`

**Service**: `ProductService`  
Returns the top-selling products in a specified date range. Sorts by `totalSold` and returns up to the `userLimit` specified.