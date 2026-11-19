package com.ecommerce.inventory.repository;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.inventory.entity.InventoryReservation;

import jakarta.persistence.LockModeType;

public interface ReservationRepository extends JpaRepository<InventoryReservation, Long>{
Optional<InventoryReservation> findByOrderId(String orderId);
boolean existsByOrderId(String orderId);
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query(""" 
		
		SELECT r FROM InventoryReservation r WHERE r.orderId = :orderId 
		""")
Optional<InventoryReservation> findByOrderIdForUpdate(@Param("orderId") String orderId);
}
