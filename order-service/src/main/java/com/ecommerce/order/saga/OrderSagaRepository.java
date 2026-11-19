package com.ecommerce.order.saga;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSagaRepository extends JpaRepository<SagaState, Long>{
Optional<SagaState> findBySagaId(String sagaId);
Optional<SagaState> findByOrderNumber(String orderNumber);
}
