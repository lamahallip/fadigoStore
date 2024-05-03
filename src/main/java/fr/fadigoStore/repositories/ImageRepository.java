package fr.fadigoStore.repositories;

import fr.fadigoStore.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    Optional<Image> findByUrl(String url);
    List<Image> findByProductId(Integer productId);

}
