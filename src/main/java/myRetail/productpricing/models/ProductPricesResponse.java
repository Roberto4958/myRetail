package myRetail.productpricing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ProductPricesResponse {

    @JsonProperty("product_id")
    private int productId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("current_prices")
    private List<Price> currentPrices;

    public ProductPricesResponse(int productId, String productName, List<Price> currentPrices) {
        this.productId = productId;
        this.productName = productName;
        this.currentPrices = currentPrices;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Price> getCurrentPrices() {
        return currentPrices;
    }

    public void setCurrentPrices(List<Price> currentPrices) {
        this.currentPrices = currentPrices;
    }
}
