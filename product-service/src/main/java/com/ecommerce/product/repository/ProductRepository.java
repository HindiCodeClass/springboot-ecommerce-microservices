package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
		Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
		Page<Product> findByPriceBetween(Double min, Double max, Pageable pageable);
		Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
		
		@Query("SELECT p FROM Product p "+
		"WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%')) "+
				"OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword,'%'))")
		Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

		@Query("SELECT p FROM Product p "+
		"WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword,'%'))) "+
				"AND (:categoryId IS NULL OR p.category.id= :categoryId) "+
		"AND (p.price BETWEEN :minPrice AND :maxPrice)")
		Page<Product> advanceFilter(
				@Param("keyword") String keyword,
				@Param("categoryId") Long categoryId,
				@Param("minPrice") Double minPrice,
				@Param("maxPrice") Double maxPrice,
				Pageable pageable
				);
		@EntityGraph(attributePaths = {"category"})
		@Query("SELECT p FROM Product p")
		Page<Product> findAllWithCategory(Pageable pageable);
		@Query("""
				SELECT p FROM Product p WHERE (:cursor IS NULL OR p.id> :cursor) ORDER BY id ASC
				""")
		List<Product> findProductsByCursor(@Param("cursor") Long cursor, Pageable pageable);
}
