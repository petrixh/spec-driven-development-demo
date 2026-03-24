package com.example.specdriven.data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockEventRepository extends JpaRepository<StockEvent, Long> {

    List<StockEvent> findTop10ByOrderByTimestampDesc();

    boolean existsByProductId(Long productId);
}
