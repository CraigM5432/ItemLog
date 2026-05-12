package com.itemlog.itemlogapi.repository;

import com.itemlog.itemlogapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository for the Transaction entity.
// Provides built-in CRUD methods through JpaRepository.
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Finds all transactions for an event, ordered with the newest sale first.
    List<Transaction> findByEvent_EventIdOrderBySaleTimeDesc(Integer eventId);

    // Checks whether an item has any transaction records.
    // Used to block deleting an item that has already been sold.
    boolean existsByItem_ItemId(Integer itemId);
}

