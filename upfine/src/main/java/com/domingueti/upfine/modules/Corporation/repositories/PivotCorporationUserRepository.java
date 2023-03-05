package com.domingueti.upfine.modules.Corporation.repositories;

import com.domingueti.upfine.modules.Corporation.models.PivotCorporationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PivotCorporationUserRepository extends JpaRepository<PivotCorporationUser, Long> {

    @Modifying
    @Query(value = "" +
        "UPDATE tb_pivot_corporation_user pivot " +
        "SET pivot.deleted_at = CURRENT_TIMESTAMP " +
        "WHERE pivot.user_id = :userId " +
        "AND deleted_at IS NULL",
        nativeQuery = true)
    void deleteAllByUserId(Long userId);
}
