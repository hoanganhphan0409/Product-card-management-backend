package Main.repository;

import Main.model.ProductCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductCardRepository extends JpaRepository<ProductCard, Long> {
    List<ProductCard> findByNameContainingIgnoreCase(String name);
    List<ProductCard> findByCategory(String category);
    List<ProductCard> findByNameContainingIgnoreCaseAndCategory(String name, String category);
}