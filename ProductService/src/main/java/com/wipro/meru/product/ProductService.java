package com.wipro.meru.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public void addProduct(Product product) {

        productRepository.save(product);
    }

    public Product getProductDetails(int productId) {
       Optional<Product> productOptional =  productRepository.findById(productId);
           if(!productOptional.isPresent()){
                throw new ProductNotFound("Product not available !!!");
           }
           Product product = productOptional.get();
           return product;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList;
    }
    
	public Product updateProduct(int productId, Product product) {
		
		Optional<Product> productOpt = productRepository.findById(productId);
		if (!productOpt.isPresent()) {
			throw new ProductNotFound("ProductId --" +productId);
		}
	
		Product productUpdated = productOpt.get();
		
		productUpdated.setProductPrice(product.getProductPrice());
		productUpdated.setProductName(product.getProductName());
		productUpdated.setProductDiscount(product.getProductDiscount());
		productUpdated.setProductOffer(product.isProductOffer());
		
		productRepository.save(productUpdated);
		
		return productUpdated;	

	}
	
	public Product deleteProduct(int productId) {
		
		Optional<Product> productOpt = productRepository.findById(productId);
		if (!productOpt.isPresent()) {
			throw new ProductNotFound("ProductId --" +productId);
		}
		
		Product productDeleted = productOpt.get();
		
		productRepository.delete(productDeleted);
		
		return productDeleted;
		
	}
}
