package myRetail.productpricing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document("price")
public class Price {
    @JsonProperty("price_id")
    @ApiModelProperty(hidden = true)
    @Id
    private String id;
    @Field("product_id")
    @JsonProperty("product_id")
    private Integer productId;
    private Double value;
    @JsonProperty("currency_code")
    private String currencyCode;

    public Price() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode==null? null: currencyCode.toUpperCase();
    }
}
