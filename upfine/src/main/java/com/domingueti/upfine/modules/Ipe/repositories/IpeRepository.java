package com.domingueti.upfine.modules.Ipe.repositories;

import com.domingueti.upfine.modules.Ipe.models.Ipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IpeRepository extends JpaRepository<Ipe, Long> {

    Optional<Ipe> findTop1ByDeletedAtIsNullOrderByReferenceDateDescIdDesc();

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

    @Query(value = "" +
        "SELECT ipe.* FROM tb_ipe ipe " +
        "WHERE ipe.id NOT IN (SELECT rel.ipe_id FROM tb_relevant_fact rel WHERE rel.deleted_at IS NULL) " +
        "AND ipe.deleted_at IS NULL",
    nativeQuery = true)
    List<Ipe> findByNonExistingRelevantFactAndDeletedAtIsNull();
}
