package com.adobe.bookstore.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStockRepository extends JpaRepository<BookStock, String> {
}
