package com.domingueti.upfine.modules.Corporation.repositories;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {
}
