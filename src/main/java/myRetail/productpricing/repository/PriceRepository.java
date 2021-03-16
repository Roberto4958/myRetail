package myRetail.productpricing.repository;

import myRetail.productpricing.models.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends MongoRepository<Price, String> {

    public List<Price> findByProductId(int productId);
    public Optional<Price> findByProductIdAndCurrencyCode(int productId, String currencyCode);
}
