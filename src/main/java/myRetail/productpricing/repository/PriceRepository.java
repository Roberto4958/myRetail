package myRetail.productpricing.repository;

import myRetail.productpricing.models.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PriceRepository extends MongoRepository<Price, String> {

    public Optional<Price> findByProductId(int productId);
}
