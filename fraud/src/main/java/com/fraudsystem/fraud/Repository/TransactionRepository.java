package com.fraudsystem.fraud.Repository;

import com.fraudsystem.fraud.Entity.Account;
import com.fraudsystem.fraud.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    public int countByAccountFromAndDate(Account account, Date date);
    @Query(
            value = "SELECT COALESCE(AVG(t.amount), 0) " +
                    "FROM transaction t " +
                    "WHERE t.account_from = :accountId " +
                    "AND t.date >= CURRENT_DATE - INTERVAL 7 DAY",
            nativeQuery = true
    )
    double getAvgTransactionAmount7d(@Param("accountId") int accountId);
    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM transaction t " +
                    "WHERE t.account_from = :accountId " +
                    "AND t.date >= NOW() - INTERVAL 1 HOUR",
            nativeQuery = true
    )
    int getTransactionsPerHour(@Param("accountId") int accountId);
    @Query(
            value = "SELECT COALESCE(COUNT(*), 0) / 168.0 " +
                    "FROM transaction t " +
                    "WHERE t.account_from = :accountId " +
                    "AND t.date >= CURRENT_DATE - INTERVAL 7 DAY",
            nativeQuery = true
    )
    double getAvgTransactionsPerHour7d(@Param("accountId") int accountId);


}
