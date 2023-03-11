package com.domingueti.upfine.modules.Ipe.repositories;

import com.domingueti.upfine.modules.Ipe.models.Ipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface IpeRepository extends JpaRepository<Ipe, Long> {

    Optional<Ipe> findTop1ByOrderByReferenceDateDesc();

    @Modifying
    @Query(value = "" +
        "UPDATE tb_ipe " +
        "SET deleted_at = CURRENT_TIMESTAMP " +
        "WHERE corporation_id = :corporationId " +
        "AND subject = :subject " +
        "AND link = :link " +
        "AND reference_date = :referenceDate " +
        "AND deleted_at IS NULL",
        nativeQuery = true)
    void deleteRepeated(Long corporationId, String subject, String link, LocalDate referenceDate);
}
