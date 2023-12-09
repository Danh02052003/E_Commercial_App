package vlu.mobileproject.modle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Products implements Serializable {
    private int product_categoryId;
    private String product_createdDate;
    private String product_description;
    private int product_id;
    private String product_img;
    private Map<String, MemoryOption> product_memoryOptions;
    private String product_name;

    // Product key in firebase - the actual ID
    String ProductID;

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public Products() {
        product_memoryOptions = new HashMap<>();
    }

    public Products(int product_categoryId, String product_createdDate, String product_description, int product_id, String product_img, Map<String, MemoryOption> product_memoryOptions, String product_name) {
        this.product_categoryId = product_categoryId;
        this.product_createdDate = product_createdDate;
        this.product_description = product_description;
        this.product_id = product_id;
        this.product_img = product_img;
        this.product_memoryOptions = product_memoryOptions;
        this.product_name = product_name;
    }

    public int getProduct_categoryId() {

        return product_categoryId;
    }

    public void setProduct_categoryId(int product_categoryId) {
        this.product_categoryId = product_categoryId;
    }

    public String getProduct_createdDate() {
        return product_createdDate;
    }

    public void setProduct_createdDate(String product_createdDate) {
        this.product_createdDate = product_createdDate;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public Map<String, MemoryOption> getProduct_memoryOptions() {
        return product_memoryOptions;
    }

    public void setProduct_memoryOptions(Map<String, MemoryOption> product_memoryOptions) {
        this.product_memoryOptions = product_memoryOptions;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
//    public double getPriceForMemory(int memorySize) {
//        for (MemoryOption option : product_memoryOptions.values()) {
//            if (option.getMemory() == memorySize) {
//                return option.getProduct_price();
//            }
//        }
//
//        return -1;
//    }
    public double getPriceForMemory() {
        Map.Entry<String, MemoryOption> entry = product_memoryOptions.entrySet().iterator().next();
        MemoryOption firstMemoryOption = entry.getValue();
        return firstMemoryOption.getProduct_price();

    }

    public double getPriceForMemory(String memory) {
        for (MemoryOption option : product_memoryOptions.values()) {
            if (option.getMemory().equals(memory)) {
                return option.getProduct_price();
            }
        }
        return 0.0;
    }

    public MemoryOption getProductOptPackage(String memoryOptKey) {
        for (String optionName : product_memoryOptions.keySet()) {
            if (optionName.equals(memoryOptKey)) {
                return product_memoryOptions.get(optionName);
            }
        }
        return null;
    }

    public int getQuantityForMemory(String memory) {
        for (MemoryOption option : product_memoryOptions.values()) {
            if (option.getMemory() == memory) {
                return option.getQuantity();
            }
        }
        return 0;
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (MemoryOption option : product_memoryOptions.values()) {
            totalQuantity += option.getQuantity();
        }
        return totalQuantity;
    }

    public String[] getMemory(){
        String[] memory = new String[product_memoryOptions.size()];
        int i = 0;
        for (MemoryOption option : product_memoryOptions.values()) {
            memory[i] = String.valueOf(option.getMemory());
            i++;
        }
        return memory;
    }

    public String[] getMemoryOptionNames(){
        String[] memoryOptName = new String[product_memoryOptions.size()];
        int i = 0;
        for (String option : product_memoryOptions.keySet()) {
            memoryOptName[i] = String.valueOf(option);
            i++;
        }
        return memoryOptName;
    }

    public static class MemoryOption implements Serializable {
        private String memory;
        private double product_price;
        private int quantity;

        public MemoryOption() {
        }

        public MemoryOption(String memory, double product_price, int quantity) {
            this.memory = memory;
            this.product_price = product_price;
            this.quantity = quantity;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public double getProduct_price() {
            return product_price;
        }

        public void setProduct_price(double product_price) {
            this.product_price = product_price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
