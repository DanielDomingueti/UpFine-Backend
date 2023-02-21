package com.domingueti.upfine.modules.Ipe.repositories;

import com.domingueti.upfine.modules.Ipe.models.Ipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface IpeRepository extends JpaRepository<Ipe, Long> {

    Optional<Ipe> findTop1ByOrderByReferenceDateDesc();

}
