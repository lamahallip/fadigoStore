package fr.fadigoStore.service;

import fr.fadigoStore.entities.Product;
import fr.fadigoStore.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Method to save Product
    public Product saveProduct(Product product) {
        log.info("Product is saving...");
        return productRepository.save(product);
    }

    // Method to update Product
    public Product updateProduct(Integer id, Product product) {

        Product productExiting = this.productRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("This product not exit"));


        productExiting.setName(product.getName());
        productExiting.setDescription(product.getDescription());
        productExiting.setParentProduct(product.getParentProduct());
        productExiting.setSubProducts(product.getSubProducts());
        productExiting.setImages(product.getImages());

        log.info("Product is updating...");

        return this.productRepository.save(productExiting);
    }

    // Method to retrieve a product
    public Product getProduct(Integer id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This product not exit"));
    }

    // Method to get All Product
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    // Method to delete a product
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }


}
