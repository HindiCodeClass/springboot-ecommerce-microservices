package com.ecommerce.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;

@SpringBootTest
@ActiveProfiles("test")
public class InventoryConcurrencyTest {
	@Autowired
	private InventoryRepository inventoryRepo;
	@Autowired
	private InventoryService inventoryService;

	@Test 

	void testDistributedLockOnConcurrentReservations() throws InterruptedException { 

	 String testSku = "LAPTOP001"; 

	 

	 // 1. Test shuru hone se pehle DB mein actual stock check karein 

	 Inventory initialInv = inventoryRepo.findBySkuCode(testSku).orElseThrow(); 

	 System.out.println("=== TEST START: Initial Available Stock = " + initialInv.getAvailableQuantity() + " ==="); 

	 

	 int numberOfThreads = 10; 

	 ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads); 

	 CountDownLatch latch = new CountDownLatch(1); 

	 

	 AtomicInteger successCount = new AtomicInteger(0); 

	 AtomicInteger failureCount = new AtomicInteger(0); 
	 for (int i = 1; i <= numberOfThreads; i++) { 
	 	final String orderId = "TEST-ORDER-" + UUID.randomUUID(); 
	 executorService.submit(() -> { 
	 try { 
	 latch.await(); 
	 inventoryService.reserve(orderId, testSku, 1); 

	 System.out.println(Thread.currentThread().getName() + " -> SUCCESS for " + orderId); 

	 successCount.incrementAndGet(); 

	 } catch (Exception e) { 
failureCount.incrementAndGet(); 

	 } 

	 }); 
	 } 
	 latch.countDown(); 
	 executorService.shutdown(); 
	 executorService.awaitTermination(15, TimeUnit.SECONDS); 
     Inventory finalInv = inventoryRepo.findBySkuCode(testSku).orElseThrow(); 

	 assertEquals(1, successCount.get(), "1reservation"); 

	 assertEquals(9, failureCount.get(), "9 fail"); 

	} 
}
