package com.adobe.bookstore.book_order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookOrderRepository extends JpaRepository<BookOrder, BookOrderId>{
    
}
