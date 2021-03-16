package myRetail.productpricing;

import myRetail.productpricing.controllers.PriceController;
import myRetail.productpricing.helpers.GlobalHelper;
import myRetail.productpricing.helpers.PriceHelpers;
import myRetail.productpricing.helpers.PriceTestHelpers;
import myRetail.productpricing.helpers.RestTemplateWrapper;
import myRetail.productpricing.models.*;
import myRetail.productpricing.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PriceController.class, PriceHelpers.class})
public class PricesUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplateWrapper restTemplate;

    @MockBean
    private PriceRepository priceRepository;

    @Test
    public void getPrice_success() throws Exception {
        Price price = PriceTestHelpers.getPrice();
        ResponseEntity responseEntity = PriceTestHelpers.getSuccessfulProductResponseEntity();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.of(price));

        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        this.mockMvc.perform(get("/54456119/price/currency/usd"))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_id").value(54456119))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_name").value("Creamy Peanut Butter 40oz - Good &#38; Gather&#8482"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.value").value(23.23))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.currency_code").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.price_id").value("54456119"));
    }

    @Test
    public void getPrice_priceNotFound() throws Exception {
        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/54456119/price/currency/fake-currency"))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Price not found."));
    }

    @Test
    public void getPrice_redskyServiceThrows404() throws Exception {
        Price price = PriceTestHelpers.getPrice();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.of(price));
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.mockMvc.perform(get("/3445/price/currency/usd"))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void getPrice_redskyServiceThrows500() throws Exception {
        Price price = PriceTestHelpers.getPrice();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.of(price));
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.mockMvc.perform(get("/3445/price/currency/usd"))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void getPrices_success() throws Exception {
        List<Price> prices = PriceTestHelpers.getPrices();
        ResponseEntity responseEntity = PriceTestHelpers.getSuccessfulProductResponseEntity();

        Mockito.when(priceRepository.findByProductId(any(int.class))).thenReturn(prices);

        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        this.mockMvc.perform(get("/9287397/prices"))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_id").value(9287397))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_name").value("Creamy Peanut Butter 40oz - Good &#38; Gather&#8482"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_prices[0].value").value(43.34))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_prices[0].currency_code").value("USD"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_prices[0].price_id").value("9872934"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_prices[1].value").value(0.043))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_prices[1].currency_code").value("BITCOIN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_prices[1].price_id").value("7987223"));
    }

    @Test
    public void getPrices_redskyServiceThrows404() throws Exception {
        List<Price> prices = PriceTestHelpers.getPrices();

        Mockito.when(priceRepository.findByProductId(any(int.class))).thenReturn(prices);
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.mockMvc.perform(get("/9287397/prices"))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void getPrices_redskyServiceThrows500() throws Exception {
        List<Price> prices = PriceTestHelpers.getPrices();

        Mockito.when(priceRepository.findByProductId(any(int.class))).thenReturn(prices);
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.mockMvc.perform(get("/9287397/prices"))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void createPrice_success() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        ResponseEntity responseEntity = PriceTestHelpers.getSuccessfulProductResponseEntity2();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.empty());
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_id").value(3453344))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_name").value("The Big Lebowski (Blu-ray)"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.value").value(34.23))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.currency_code").value("BITCOIN"));
    }

    @Test
    public void createPrice_badCurrencyValue() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setValue(null);

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Please include all fields: product_id, value, currency_code."));
    }

    @Test
    public void createPrice_badCurrencyCode() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setCurrencyCode(null);

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Please include all fields: product_id, value, currency_code."));
    }

    @Test
    public void createPrice_badProductId() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setProductId(null);

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Please include all fields: product_id, value, currency_code."));
    }

    @Test
    public void createPrice_ProductIdAlreadyExists() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.of(price));

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("There is already a price with that product_id and currency_code."));
    }

    @Test
    public void createPrice_redskyServiceThrows404() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.empty());
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void createPrice_redskyServiceThrows500() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.empty());
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.mockMvc.perform(post("/price")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void updatePrice_success() throws Exception {
        Price body = PriceTestHelpers.getPriceWithoutId();
        Price price = PriceTestHelpers.getPriceWithoutId();
        body.setValue(0.0000022);
        price.setId("234235");

        ResponseEntity responseEntity = PriceTestHelpers.getSuccessfulProductResponseEntity2();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class)))
                .thenReturn(Optional.of(price)).thenReturn(Optional.empty());
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(body)))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_id").value(3453344))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product_name").value("The Big Lebowski (Blu-ray)"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.value").value(0.0000022))
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_price.currency_code").value("BITCOIN"));
    }

    @Test
    public void updatePrice_badCurrencyValue() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setValue(null);

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Please include all fields: product_id, value, currency_code."));
    }

    @Test
    public void updatePrice_badCurrencyCode() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setCurrencyCode(null);

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Please include all fields: product_id, value, currency_code."));
    }

    @Test
    public void updatePrice_badProductId() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setProductId(null);

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Please include all fields: product_id, value, currency_code."));
    }

    @Test
    public void updatePrice_redskyServiceThrows404() throws Exception {
        Price body = PriceTestHelpers.getPriceWithoutId();
        Price price = PriceTestHelpers.getPriceWithoutId();
        body.setValue(0.0000022);
        price.setId("234235");

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.of(price));
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(body)))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void updatePrice_redskyServiceThrows500() throws Exception {
        Price body = PriceTestHelpers.getPriceWithoutId();
        Price price = PriceTestHelpers.getPriceWithoutId();
        body.setValue(0.0000022);
        price.setId("234235");

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class))).thenReturn(Optional.of(price));
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE));

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(body)))
                .andExpect(status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("Product name not found."));
    }

    @Test
    public void updatePrice_priceAlreadyExist() throws Exception {
        Price price = PriceTestHelpers.getPriceWithoutId();
        price.setProductId(809786987);
        Price priceWithId = PriceTestHelpers.getPrice();
        ResponseEntity responseEntity = PriceTestHelpers.getSuccessfulProductResponseEntity2();

        Mockito.when(priceRepository.findByProductIdAndCurrencyCode(any(int.class), any(String.class)))
                .thenReturn(Optional.of(price)).thenReturn(Optional.of(priceWithId));
        Mockito.when(restTemplate.exchange(
                any(String.class), any(HttpMethod.GET.getClass()), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        this.mockMvc.perform(put("/3453344/price/currency/bitcoin")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(GlobalHelper.asJsonString(price)))
                .andExpect(status().is(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_code").value(409))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_message").value("There already exist a Price with that product_id and currency_code."));
    }

}
