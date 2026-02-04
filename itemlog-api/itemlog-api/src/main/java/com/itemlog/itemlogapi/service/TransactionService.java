package com.itemlog.itemlogapi.service;

import com.itemlog.itemlogapi.dto.CreateTransactionRequest;
import com.itemlog.itemlogapi.entity.Event;
import com.itemlog.itemlogapi.entity.Item;
import com.itemlog.itemlogapi.entity.Transaction;
import com.itemlog.itemlogapi.exception.NotFoundException;
import com.itemlog.itemlogapi.repository.EventRepository;
import com.itemlog.itemlogapi.repository.ItemRepository;
import com.itemlog.itemlogapi.repository.TransactionRepository;
import com.itemlog.itemlogapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final ItemRepository itemRepo;
    private final TransactionRepository txRepo;

    public TransactionService(UserRepository userRepo,
                              EventRepository eventRepo,
                              ItemRepository itemRepo,
                              TransactionRepository txRepo) {
        this.userRepo = userRepo;
        this.eventRepo = eventRepo;
        this.itemRepo = itemRepo;
        this.txRepo = txRepo;
    }

    public List<Transaction> listTransactions(Integer userId, Integer eventId) {
        getEventOwnedByUser(userId, eventId);
        return txRepo.findByEvent_EventIdOrderBySaleTimeDesc(eventId);
    }

    @Transactional
    public Transaction createTransaction(Integer userId, Integer eventId, CreateTransactionRequest request) {
        Event event = getEventOwnedByUser(userId, eventId);

        Item item = itemRepo.findByItemIdAndEvent_EventId(request.getItemId(), eventId)
                .orElseThrow(() -> new NotFoundException("Item not found for this event."));

        int sold = request.getQuantitySold();
        if (item.getQuantity() == null) {
            throw new IllegalArgumentException("Item quantity is not set.");
        }
        if (sold > item.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock to complete sale.");
        }

        // decrement stock
        item.setQuantity(item.getQuantity() - sold);
        itemRepo.save(item);

        BigDecimal salePrice = request.getSalePrice() != null ? request.getSalePrice() : item.getPrice();
        if (salePrice == null) {
            throw new IllegalArgumentException("Item price is not set and salePrice was not provided.");
        }

        Transaction tx = new Transaction();
        tx.setEvent(event);
        tx.setItem(item);
        tx.setQuantitySold(sold);
        tx.setSalePrice(salePrice);
        tx.setSaleTime(LocalDateTime.now());

        return txRepo.save(tx);
    }

    public String exportTransactionsCsv(Integer userId, Integer eventId) {
        List<Transaction> txs = listTransactions(userId, eventId);

        StringBuilder sb = new StringBuilder();
        sb.append("transactionId,eventId,itemId,itemName,quantitySold,salePrice,totalAmount,saleTime\n");

        for (Transaction t : txs) {
            BigDecimal total = t.getSalePrice().multiply(BigDecimal.valueOf(t.getQuantitySold()));
            sb.append(safe(t.getTransactionId())).append(',')
                    .append(safe(t.getEvent().getEventId())).append(',')
                    .append(safe(t.getItem().getItemId())).append(',')
                    .append(csvEscape(t.getItem().getName())).append(',')
                    .append(safe(t.getQuantitySold())).append(',')
                    .append(safe(t.getSalePrice())).append(',')
                    .append(safe(total)).append(',')
                    .append(csvEscape(String.valueOf(t.getSaleTime())))
                    .append('\n');
        }

        return sb.toString();
    }

    private Event getEventOwnedByUser(Integer userId, Integer eventId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException("User not found.");
        }
        return eventRepo.findByEventIdAndUser_UserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found for this user."));
    }

    private String safe(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private String csvEscape(String s) {
        if (s == null) return "";
        boolean mustQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String out = s.replace("\"", "\"\"");
        return mustQuote ? "\"" + out + "\"" : out;
    }
}
