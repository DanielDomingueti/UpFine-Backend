package com.domingueti.upfine.modules.RelevantFact.repositories;

import com.domingueti.upfine.modules.RelevantFact.daos.RelevantFactIpeDAO;
import com.domingueti.upfine.modules.RelevantFact.models.RelevantFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RelevantFactRepository extends JpaRepository<RelevantFact, Long> {

    @Query(value = ""
            + "SELECT corp.name, corp.cnpj, ipe.subject, ipe.reference_date, fac.summarized " +
            "FROM tb_relevant_fact fac " +
            "INNER JOIN tb_ipe ipe ON ipe.id = fac.ipe_id " +
            "INNER JOIN tb_corporation corp ON corp.id = ipe.corporation_id " +
            "WHERE ipe.reference_date = CURRENT_DATE-7 " +
            "AND fac.deleted_at IS NULL " +
            "AND ipe.deleted_at IS NULL " +
            "AND corp.deleted_at IS NULL",
        nativeQuery = true)
    List<RelevantFactIpeDAO> findNameAndCnpjAndRelevantFactAndSubjectAndDateOfToday();

}
