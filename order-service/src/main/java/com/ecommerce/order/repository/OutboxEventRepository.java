package com.ecommerce.order.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.order.client.OutboxStatus;
import com.ecommerce.order.order.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long>{
	List<OutboxEvent> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);

}
