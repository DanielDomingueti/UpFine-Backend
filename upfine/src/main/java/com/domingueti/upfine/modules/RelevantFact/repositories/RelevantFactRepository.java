package com.domingueti.upfine.modules.RelevantFact.repositories;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RelevantFactRepository extends JpaRepository<RelevantFact, Long> {

    @Query(value = ""
            + "SELECT u.name AS user, corp.name AS corporation, corp.cnpj, ipe.subject, ipe.reference_date as date, fac.summarized, ipe.link " +
            "FROM tb_relevant_fact fac " +
            "INNER JOIN tb_ipe ipe ON ipe.id = fac.ipe_id " +
            "INNER JOIN tb_corporation corp ON corp.id = ipe.corporation_id " +
            "INNER JOIN tb_user u ON u.id = :userId " +
            "WHERE ipe.reference_date <= CURRENT_DATE " +
            "AND fac.deleted_at IS NULL " +
            "AND ipe.deleted_at IS NULL " +
            "AND corp.deleted_at IS NULL " +
            "LIMIT 10",
        nativeQuery = true)
    List<RelevantFactIpeDAO> findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday(Long userId);

    @Query(value = ""
            + "SELECT u.name AS user, corp.name AS corporation, corp.cnpj, ipe.subject, ipe.reference_date as date, fac.summarized, ipe.link " +
            "FROM tb_relevant_fact fac " +
            "INNER JOIN tb_ipe ipe ON ipe.id = fac.ipe_id " +
            "INNER JOIN tb_corporation corp ON corp.id = ipe.corporation_id " +
            "INNER JOIN tb_pivot_user_corporation pivot ON pivot.user_id = :userId " +
            "INNER JOIN tb_user u ON u.id = pivot.user_id " +
            "WHERE ipe.reference_date <= CURRENT_DATE " +
            "AND pivot.corporation_id = corp.id " +
            "AND fac.deleted_at IS NULL " +
            "AND ipe.deleted_at IS NULL " +
            "AND corp.deleted_at IS NULL " +
            "LIMIT 10",
            nativeQuery = true)
    List<RelevantFactIpeDAO> findNameAndCnpjAndRelevantFactAndSubjectAndDateOfTodayByUserId(Long userId);
}
