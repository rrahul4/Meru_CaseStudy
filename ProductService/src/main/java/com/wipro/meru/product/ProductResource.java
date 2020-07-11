package com.wipro.meru.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public void addProduct(@RequestBody Product product){
        productService.addProduct(product);
        //TODO :call inventory service to save product id and update quantity in inventory DB.
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/products/{productId}")
    public Product getProductDetails(@PathVariable int productId){
        Product productDetail = productService.getProductDetails(productId);
        return productDetail;
    }
    
	@PutMapping("/products/{productId}")
	public void updateProduct(@PathVariable int productId, @RequestBody Product product) {
		
		Product updatedProduct = productService.updateProduct(productId, product);
		
		if (updatedProduct.getProductId() == 0) {
			throw new ProductNotFound("ProductId --" +productId);
		}
	}
	
	
	@DeleteMapping("/products/{productId}")
	public void deleteProducts(@PathVariable int productId) {
		
		Product deletedProduct = productService.deleteProduct(productId);
		
		if (deletedProduct.getProductId() == 0) {
			throw new ProductNotFound("ProductId --" +productId);
		}	
	}
	
}
