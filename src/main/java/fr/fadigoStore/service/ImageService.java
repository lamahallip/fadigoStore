package fr.fadigoStore.service;

import fr.fadigoStore.entities.Image;
import fr.fadigoStore.entities.Product;
import fr.fadigoStore.repositories.ImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    public Image add(Integer id, Image image) {

        Product product = this.productService.getProduct(id);
        image.setProduct(product);

        return this.imageRepository.save(image);
    }

    // Method to save Image
    public Image saveImage(Image image) {

        Image imageNew = Image
                .builder()
                .type(image.getType())
                .url(image.getUrl())
                .build();

        return this.imageRepository.save(imageNew);
    }

    // Method to get All Image
    public List<Image> getAllImages() {
        return this.imageRepository.findAll();
    }

    // Method to get Product Images
    public List<Image> getImagesProduct(Integer productId) {
        return this.imageRepository.findByProductId(productId);
    }

    // Method to update Image
    public Image updateImage(Integer id, Image image) {
        Image imageExisting = this.imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("This image not existing"));

        imageExisting.setType(image.getType());
        imageExisting.setUrl(image.getUrl());

        return this.imageRepository.save(imageExisting);
    }

    // Method to delete Image
    public void deleteImage(Integer id) {
        this.imageRepository.deleteById(id);
    }

}
