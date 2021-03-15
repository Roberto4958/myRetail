package myRetail.productpricing.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductPriceResponse {

    @JsonProperty("product_id")
    private int productId;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("current_price")
    private Price currentPrice;


    public ProductPriceResponse(int productId, String productName, Price currentPrice) {
        this.productId = productId;
        this.productName = productName;
        this.currentPrice = currentPrice;
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

    public Price getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Price currentPrice) {
        this.currentPrice = currentPrice;
    }
}
