package com.itemlog.itemlogapi.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Entity representing a recorded sale.
// Each transaction is linked to both an event and an item.
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity_sold", nullable = false)
    private Integer quantitySold;

    // Stored per transaction so historical sales remain accurate if item prices change later.
    @Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "sale_time", nullable = false)
    private LocalDateTime saleTime;

    // Empty constructor required by JPA.
    public Transaction() {}

    public Integer getTransactionId() { return transactionId; }
    public void setTransactionId(Integer transactionId) { this.transactionId = transactionId; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public Integer getQuantitySold() { return quantitySold; }
    public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }

    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }

    public LocalDateTime getSaleTime() { return saleTime; }
    public void setSaleTime(LocalDateTime saleTime) { this.saleTime = saleTime; }
}