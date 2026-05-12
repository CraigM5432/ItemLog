package com.itemlog.itemlogapi.controller;

import com.itemlog.itemlogapi.dto.CreateTransactionRequest;
import com.itemlog.itemlogapi.dto.TransactionResponse;
import com.itemlog.itemlogapi.entity.Transaction;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.security.CurrentUser;
import com.itemlog.itemlogapi.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Handles transaction recording, transaction history, and CSV export for an event.
// Transaction business rules are handled by TransactionService.
@RestController
@RequestMapping("/events/{eventId}/transactions")
public class TransactionController {

    private final TransactionService txService;

    public TransactionController(TransactionService txService) {
        this.txService = txService;
    }

    private static TransactionResponse toResponse(Transaction t) {
        BigDecimal total = t.getSalePrice().multiply(BigDecimal.valueOf(t.getQuantitySold()));

        return new TransactionResponse(
                t.getTransactionId(),
                t.getEvent().getEventId(),
                t.getItem().getItemId(),
                t.getItem().getName(),
                t.getQuantitySold(),
                t.getSalePrice(),
                total,
                t.getSaleTime()
        );
    }

    private Integer currentUserId() {
        Integer userId = CurrentUser.id();

        if (userId == null) {
            throw new NotFoundException("User not found.");
        }

        return userId;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list(@PathVariable Integer eventId) {
        Integer userId = currentUserId();

        List<TransactionResponse> result = txService.listTransactions(userId, eventId).stream()
                .map(TransactionController::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@PathVariable Integer eventId,
                                                      @Valid @RequestBody CreateTransactionRequest request) {
        Integer userId = currentUserId();

        // Creates the transaction and reduces stock through the service layer.
        Transaction saved = txService.createTransaction(userId, eventId, request);

        return ResponseEntity.status(201).body(toResponse(saved));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCsv(@PathVariable Integer eventId) {
        Integer userId = currentUserId();

        String csv = txService.exportTransactionsCsv(userId, eventId);
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"transactions_event_" + eventId + ".csv\"")
                .contentType(new MediaType("text", "csv"))
                .contentLength(bytes.length)
                .body(bytes);
    }
}

