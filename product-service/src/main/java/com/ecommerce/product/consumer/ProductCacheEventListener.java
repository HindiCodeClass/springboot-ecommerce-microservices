package com.ecommerce.product.consumer;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ecommerce.product.event.ProductCacheEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCacheEventListener {
private final CacheManager cacheManager;

@KafkaListener(topics = "product-cache-event", groupId = "product-cache-invalidation")
public void handleProductEvent(ProductCacheEvent event) {
	switch (event.eventType()){
	case "PRODUCT_CREATED" -> {
		evictProductListCaches();
	}
	case "PRODUCT_UPDATED" -> {
		evictProductCaches(event.productId());
	evictProductListCaches();
	}
	case "PRODUCT_DELETED" -> {
		evictProductCaches(event.productId());
		evictProductListCaches();}
	default ->{System.out.println("Unknown product Event");}
					
	}
}
private void evictProductCaches(Long productId) {
	Cache cache = cacheManager.getCache("productCache");
	if(cache !=null) cache.evict(productId);
}
private void evictProductListCaches() {
	Cache productPageCache = cacheManager.getCache("productPageCache");
		if(productPageCache != null) productPageCache.clear();
}
}
