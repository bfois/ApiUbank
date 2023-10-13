package com.api.Ubank.Repository;

import com.api.Ubank.Entity.Transaction;
import com.api.Ubank.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByUser(User user);
}
