package com.ecommerce.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.inventory.entity.Inventory;

import jakarta.persistence.LockModeType;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>{
		Optional<Inventory> findBySkuCode(String skuCode);
		@Lock(LockModeType.PESSIMISTIC_WRITE)
		@Query(""" 
				
				SELECT i FROM Inventory i WHERE i.skuCode = :skuCode 
				""")
		Optional<Inventory> findBySkuCodeForUpdate(@Param("skuCode") String skuCode);		
}
