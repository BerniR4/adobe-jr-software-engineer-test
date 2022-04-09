package com.adobe.bookstore.order;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.adobe.bookstore.book.BookStock;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "order")
@JsonSerialize
public class Order {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToMany
    @Column(name = "books", nullable = false)
    private List<BookStock> books;

    public Order(List<BookStock> books) {
        this.id = UUID.randomUUID().toString();
        this.books = books;
    }

    public String getId() {
        return this.id;
    }
}
