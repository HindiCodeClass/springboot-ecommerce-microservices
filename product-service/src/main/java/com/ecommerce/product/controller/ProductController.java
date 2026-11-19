package com.ecommerce.product.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.product.dto.CursorPageResponse;
import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.service.ProductService;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
		private final ProductService productService;
		@PostMapping
		public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto){
			return ResponseEntity.ok(productService.createProduct(dto));
		}
		@PutMapping("/{id}")
		public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
				@RequestBody ProductDto dto){
			return ResponseEntity.ok(productService.updateProduct(id, dto));
		}
		@DeleteMapping("/{id}")
		public ResponseEntity<String> deleteProduct(@PathVariable Long id){
			productService.deleteProduct(id);
			return ResponseEntity.ok("Product deleted successfully");
		}
		@GetMapping("/{id}")
		public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
			return ResponseEntity.ok(productService.getProductById(id));
		}
		@GetMapping
		public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDir){
			return ResponseEntity.ok(productService.getAllProduct(page, size, sortBy, sortDir));
		}
		@GetMapping("/search")
		public ResponseEntity<Page<ProductDto>> searchProducts(@RequestParam String keyword,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
			return ResponseEntity.ok(productService.searchProduct(keyword, page, size));
		}
		@GetMapping("/filter")
		public ResponseEntity<Page<ProductDto>> filterProducts(@RequestParam(required = false)Long categoryId, @RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
			return ResponseEntity.ok(productService.filterProducts(categoryId, minPrice, maxPrice, page, size));
		}
		@PostMapping("/{id}/upload-image")
		public ResponseEntity<ProductDto> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException{
			return ResponseEntity.ok(productService.uploadImage(id, file));
		}
		@GetMapping("/cursor")
		public ResponseEntity<CursorPageResponse<ProductDto>> getProductByCursor(@RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") int size){
			return ResponseEntity.ok(productService.getProductByCursor(cursor, size));
		}
}













