package com.ecommerce.product.service;

import java.io.IOError;
import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.product.dto.CursorPageResponse;
import com.ecommerce.product.dto.ProductDto;

public interface ProductService {
		ProductDto createProduct(ProductDto dto);
		ProductDto updateProduct(Long id, ProductDto dto);
		void deleteProduct(Long id);
		ProductDto getProductById(Long id);
		ProductDto uploadImage(Long productId, MultipartFile file) throws IOException;
		Page<ProductDto> getAllProduct(int page, int size, String sortBy, String sortDir);
		Page<ProductDto> searchProduct(String keyword, int page,int size);
		Page<ProductDto> filterProducts(Long categoryId,Double minPrice, Double maxPrice, int page, int size);
		Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir);
		CursorPageResponse<ProductDto> getProductByCursor(Long cursor, int size);
}
