package com.domingueti.upfine.modules.MarketData.repositories;

import com.domingueti.upfine.modules.MarketData.models.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketDataRepository extends JpaRepository<MarketData, Long> {
}
