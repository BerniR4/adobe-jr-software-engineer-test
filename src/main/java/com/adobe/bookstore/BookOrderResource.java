package com.adobe.bookstore;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookOrderResource {

    private BookOrderRepository bookOrderRepository;

    @Autowired
    public BookOrderResource(BookOrderRepository bookOrderRepository) {
        this.bookOrderRepository = bookOrderRepository;
    }

    @GetMapping("orders")
    public List<BookOrder> all() {
        return bookOrderRepository.findAll();
    }
    
    @PostMapping("order")
    public String newOrder(@RequestBody List<BookStock> requestedBooks) {
        BookOrder order = new BookOrder(requestedBooks);
        //bookOrderRepository.save(order);
        return order.getId();
    }
}
