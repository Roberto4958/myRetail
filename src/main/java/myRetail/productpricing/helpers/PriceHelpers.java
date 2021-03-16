package myRetail.productpricing.helpers;

import myRetail.productpricing.models.Item;
import myRetail.productpricing.models.Price;
import myRetail.productpricing.models.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
public class PriceHelpers {

    @Autowired
    private RestTemplateWrapper restTemplate;

    @PostConstruct
    public void setRestTemplate() {
        restTemplate.setMessageConverters(Arrays.asList(
                new MappingJackson2HttpMessageConverter(),
                new ResourceHttpMessageConverter(),
                new BufferedImageHttpMessageConverter(),
                new ByteArrayHttpMessageConverter(),
                new FormHttpMessageConverter()
        ));
    }

    public String getProductName(int productId) {
        String url = String.format("https://redsky.target.com/v3/pdp/tcin/%d?excludes=taxonomy,price,promotion," +
                        "bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics" +
                        "&key=candidate", productId);
        try {
            ResponseEntity<ProductResponse> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, null),
                    ProductResponse.class);
            Item item = response.getBody().getProduct().getItem();

            if(item.getProductDescription() == null) {
                return null;
            }
            return item.getProductDescription().getTitle();
        }
        catch (HttpClientErrorException | HttpServerErrorException | UnknownHttpStatusCodeException e) {
            return null;
        }
    }

    public static String validatePrice(Price price) {
        if(price.getProductId() == null || price.getCurrencyCode() == null || price.getValue() == null)
            return "Please include all fields: product_id, value, currency_code.";
        return null;
    }
}
