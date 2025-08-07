package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.DTO.SuspectCountPerDayDTO;
import com.fraudsystem.fraud.Entity.Suspect;
import com.fraudsystem.fraud.Entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface SuspectRepository extends JpaRepository<Suspect, Integer> {
    @Query("SELECT FUNCTION('DATE', s.date) AS date, COUNT(s) FROM Suspect s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY FUNCTION('DATE', s.date)")
    List<Object[]> getSuspectCountBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query("SELECT r.rule_name, COUNT(s) FROM Suspect s LEFT JOIN Rule r on r.ruleId=s.rule.ruleId GROUP BY r.rule_name")
    List<Object[]> countAlertsPerRule();

    boolean existsByTransaction(Transaction transaction);

    @Query("SELECT s.account.account_no, COUNT(s) as alertCount " +
            "FROM Suspect s " +
            "GROUP BY s.account.account_no " +
            "ORDER BY alertCount DESC")
    List<Object[]> countTop10AlertsPerAccount(Pageable pageable);
}
