package com.ecommerce.product.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCacheRefreshService {
private final ProductRepository productRepo;
private final ProductMapper productMapper;
private final CacheManager cacheManager;
public void refreshProduct(Long productId) {
	Product product = productRepo.findById(productId).orElse(null);
	if(product== null) {
		Cache cache = cacheManager.getCache("productCache");
		if(cache != null) {
			cache.evict(productId);
		}
		return;
	}
	ProductDto dto = productMapper.toDto(product);
	Cache cache = cacheManager.getCache("productCache");
	if(cache != null) cache.put(productId, dto);
}
}
