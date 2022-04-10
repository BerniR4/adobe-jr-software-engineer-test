package com.adobe.bookstore.order;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.adobe.bookstore.book_order.BookOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "order_table")
@JsonSerialize
public class Order {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<BookOrder> bookOrders;
    
    private Order(){}

    public Order(Set<BookOrder> bookOrders, boolean status) {
        this.id = UUID.randomUUID().toString();
        this.bookOrders = bookOrders;
        this.status = status;
    }

    public String getId() {
        return this.id;
    }

    public boolean getSatus() {
        return this.status;
    }

    public Set<BookOrder> getAllBookOrders() {
        return this.bookOrders;
    }
}
