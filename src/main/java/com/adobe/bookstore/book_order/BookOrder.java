package com.adobe.bookstore.book_order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adobe.bookstore.book.BookStock;
import com.adobe.bookstore.order.Order;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "book_order")
@JsonSerialize
@IdClass(BookOrderId.class)
public class BookOrder {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "book_stock_id", referencedColumnName = "id")
    @JsonProperty("book")
    private BookStock book;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(name = "how_many", nullable = false)
    private int howMany;

    public String getBookId() {
        return book.getId();
    }

    public int getHowMany() {
        return howMany;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void addHowMany(int n) {
        this.howMany += n;
    }
}
