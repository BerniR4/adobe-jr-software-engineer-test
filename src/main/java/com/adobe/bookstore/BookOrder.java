package com.adobe.bookstore;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "order")
@JsonSerialize
public class BookOrder {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToMany
    @Column(name = "books", nullable = false)
    private List<BookStock> books;

    public BookOrder(List<BookStock> books) {
        this.id = UUID.randomUUID().toString();
        this.books = books;
    }

    public String getId() {
        return this.id;
    }
}
