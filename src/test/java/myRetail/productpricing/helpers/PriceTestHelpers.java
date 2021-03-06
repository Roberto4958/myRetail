package myRetail.productpricing.helpers;

import myRetail.productpricing.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PriceTestHelpers {

    public static ProductResponse getProductResponse() {
        ProductDescription productDescription = new ProductDescription();
        productDescription.setTitle("Creamy Peanut Butter 40oz - Good &#38; Gather&#8482");
        Item item = new Item();
        item.setProductDescription(productDescription);
        Product product = new Product();
        product.setItem(item);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProduct(product);
        return productResponse;
    }

    public static ProductResponse getProductResponse2() {
        ProductDescription productDescription = new ProductDescription();
        productDescription.setTitle("The Big Lebowski (Blu-ray)");
        Item item = new Item();
        item.setProductDescription(productDescription);
        Product product = new Product();
        product.setItem(item);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProduct(product);
        return productResponse;
    }

    public static ProductResponse getEmptyProductResponse() {
        Product product = new Product();
        product.setItem(null);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProduct(product);
        return productResponse;
    }

    public static Price getPrice() {
        Price price = new Price();
        price.setId("54456119");
        price.setProductId(809786987);
        price.setValue(23.23);
        price.setCurrencyCode("USD");
        return price;
    }

    public static Price getPriceWithoutId() {
        Price price = new Price();
        price.setProductId(3453344);
        price.setValue(34.23);
        price.setCurrencyCode("Bitcoin");
        return price;
    }


    public static ResponseEntity<ProductResponse> getSuccessfulProductResponseEntity() {
        ProductResponse productResponse = getProductResponse();
        return new ResponseEntity(productResponse, HttpStatus.OK);
    }

    public static ResponseEntity<ProductResponse> getSuccessfulProductResponseEntity2() {
        ProductResponse productResponse = getProductResponse2();
        return new ResponseEntity(productResponse, HttpStatus.OK);
    }

    public static ResponseEntity<ProductResponse> getNotFoundProductResponseEntity() {
        ProductResponse productResponse = getEmptyProductResponse();
        return new ResponseEntity(productResponse, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<ProductResponse> getServerErrorProductResponseEntity() {
        return new ResponseEntity(null, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
