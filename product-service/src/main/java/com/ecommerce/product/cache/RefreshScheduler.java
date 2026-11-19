package com.ecommerce.product.cache;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RefreshScheduler {
private final ProductCacheRefreshService refreshService;

@Scheduled(fixedDelay = 60000)
public void refreshPolularProduct() {
	refreshService.refreshProduct(545466L);
}
}
