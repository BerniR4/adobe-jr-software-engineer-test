package com.adobe.bookstore.order;

import java.util.List;

import com.adobe.bookstore.book.BookStock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResource {

    private OrderRepository bookOrderRepository;

    @Autowired
    public OrderResource(OrderRepository bookOrderRepository) {
        this.bookOrderRepository = bookOrderRepository;
    }

    @GetMapping("orders")
    public List<Order> all() {
        return bookOrderRepository.findAll();
    }
    
    @PostMapping("order")
    public String newOrder(@RequestBody List<BookStock> requestedBooks) {
        Order order = new Order(requestedBooks);
        //bookOrderRepository.save(order);
        return order.getId();
    }
}
