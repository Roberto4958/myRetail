package myRetail.productpricing.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import myRetail.productpricing.helpers.PriceHelpers;
import myRetail.productpricing.models.ErrorMessage;
import myRetail.productpricing.models.Price;
import myRetail.productpricing.models.ProductPriceResponse;
import myRetail.productpricing.models.ProductPricesResponse;
import myRetail.productpricing.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PriceController {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceHelpers priceHelpers;


    @GetMapping("{product_id}/price/currency/{currency_code}")
    @ApiOperation("Retrieves product Price by product_id and currency_code")
    @ApiResponses({
            @ApiResponse(code=200, message="Retrieved product Price by product_id and currency_code."),
            @ApiResponse(code=404, message="Price not found. || Product name not found.")
    })
    public ResponseEntity getPrice(@PathVariable("product_id") int productId, @PathVariable("currency_code") String currencyCode) {
        // check if price exists
        Optional<Price> record = priceRepository.findByProductIdAndCurrencyCode(productId, currencyCode.toUpperCase());
        if (!record.isPresent()) {
            return new ResponseEntity(new ErrorMessage("Price not found.", 404), HttpStatus.NOT_FOUND);
        }
        Price price = record.get();
        String productName = priceHelpers.getProductName(productId);
        if(productName == null) {
            return new ResponseEntity(new ErrorMessage("Product name not found.", 404), HttpStatus.NOT_FOUND);
        }
        ProductPriceResponse productPriceResponse = new ProductPriceResponse(productId, productName, price);

        return new ResponseEntity(productPriceResponse, HttpStatus.OK);
    }

    @GetMapping("{product_id}/prices")
    @ApiOperation("Retrieves product Prices by product_id")
    @ApiResponses({
            @ApiResponse(code=200, message="Retrieved product Prices by product_id."),
            @ApiResponse(code=404, message="Product name not found.")
    })
    public ResponseEntity getPrices(@PathVariable("product_id") int productId) {
        List<Price> records = priceRepository.findByProductId(productId);

        String productName = priceHelpers.getProductName(productId);
        if(productName == null) {
            return new ResponseEntity(new ErrorMessage("Product name not found.", 404), HttpStatus.NOT_FOUND);
        }
        ProductPricesResponse productPricesResponse = new ProductPricesResponse(productId, productName, records);

        return new ResponseEntity(productPricesResponse, HttpStatus.OK);
    }

    @PostMapping("price")
    @ApiOperation("Creates a new Price.")
    @ApiResponses({
            @ApiResponse(code=201, message="Created a new Price."),
            @ApiResponse(code=400, message="Please include all fields: product_id, value, currency_code."),
            @ApiResponse(code=404, message="Product name not found."),
            @ApiResponse(code=409, message="There is already a Price with that product_id and currency_code.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createPrice(@RequestBody Price price) {

        // check if request body is valid
        String errorMessage = PriceHelpers.validatePrice(price);
        if (errorMessage != null) {
            return new ResponseEntity(new ErrorMessage(errorMessage, 400), HttpStatus.BAD_REQUEST);
        }

        // check if price already exists
        Optional<Price> record = priceRepository.findByProductIdAndCurrencyCode(price.getProductId(), price.getCurrencyCode());
        if (record.isPresent()) {
            return new ResponseEntity(
                    new ErrorMessage("There is already a price with that product_id and currency_code.", 409),
                    HttpStatus.CONFLICT);
        }

        // Check if product exist
        String productName = priceHelpers.getProductName(price.getProductId());
        if(productName == null) {
            return new ResponseEntity(new ErrorMessage("Product name not found.", 404), HttpStatus.NOT_FOUND);
        }

        priceRepository.insert(price);
        ProductPriceResponse productPriceResponse = new ProductPriceResponse(price.getProductId(), productName, price);
        return new ResponseEntity(productPriceResponse, HttpStatus.CREATED);
    }

    @PutMapping("{product_id}/price/currency/{currency_code}")
    @ApiOperation("Updates Price by product_id and currency_code")
    @ApiResponses({
            @ApiResponse(code=200, message="Updated Price by product_id and currency_code."),
            @ApiResponse(code=400, message="Please include all fields: product_id, value, currency_code."),
            @ApiResponse(code=404, message="Price not found. || Product name not found."),
            @ApiResponse(code=409, message="There already exist a Price with that product_id and currency_code.")
    })
    public ResponseEntity updatePrice(@RequestBody Price price, @PathVariable("product_id") int productId,
                                      @PathVariable("currency_code") String currencyCode) {

        // check if request body is valid
        String errorMessage = PriceHelpers.validatePrice(price);
        if (errorMessage != null) {
            return new ResponseEntity(new ErrorMessage(errorMessage, 400), HttpStatus.BAD_REQUEST);
        }
        price.setCurrencyCode(price.getCurrencyCode().toUpperCase());
        currencyCode = currencyCode.toUpperCase();

        // check if price exists
        Optional<Price> record = priceRepository.findByProductIdAndCurrencyCode(productId, currencyCode.toUpperCase());
        if (!record.isPresent()) {
            return new ResponseEntity(new ErrorMessage("Price not found.", 404), HttpStatus.NOT_FOUND);
        }

        // check if they are updating product id
        if(productId != price.getProductId() || !currencyCode.equals(price.getCurrencyCode())) {
            Optional<Price> newPrice = priceRepository.findByProductIdAndCurrencyCode(price.getProductId(), price.getCurrencyCode());
            if (newPrice.isPresent()) {
                return new ResponseEntity(
                        new ErrorMessage(
                                "There already exist a Price with that product_id and currency_code.", 409),
                        HttpStatus.CONFLICT);
            }
        }

        // Check if product exist
        String productName = priceHelpers.getProductName(price.getProductId());
        if(productName == null) {
            return new ResponseEntity(new ErrorMessage("Product name not found.", 404), HttpStatus.NOT_FOUND);
        }

        Price newPrice = record.get();
        newPrice.setProductId(price.getProductId());
        newPrice.setValue(price.getValue());
        newPrice.setCurrencyCode(price.getCurrencyCode());

        priceRepository.save(newPrice);
        ProductPriceResponse productPriceResponse = new ProductPriceResponse(price.getProductId(), productName, newPrice);

        return new ResponseEntity(productPriceResponse, HttpStatus.OK);
    }
}
