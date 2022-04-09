package com.adobe.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookOrderRepository extends JpaRepository<BookOrder, String>  {
    
}
