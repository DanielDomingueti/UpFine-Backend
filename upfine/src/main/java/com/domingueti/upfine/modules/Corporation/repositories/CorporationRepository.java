package com.domingueti.upfine.modules.Corporation.repositories;

import com.domingueti.upfine.modules.Corporation.models.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CorporationRepository extends JpaRepository<Corporation, Long> {

    Optional<Corporation> findByCnpjAndName(String cnpj, String name);

}
