package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Used for listing transactions
    List<Transaction> findByEvent_EventIdOrderBySaleTimeDesc(Integer eventId);

    // Used to block item deletion
    boolean existsByItem_ItemId(Integer itemId);
}

