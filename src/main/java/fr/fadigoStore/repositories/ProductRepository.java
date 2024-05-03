package fr.fadigoStore.repositories;

import fr.fadigoStore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByParentProductIsNull();

    Optional<Product> findByIdAndParentProductIsNull(Long productId);

    List<Product> findByParentProductId(Long parentProductId);
}
