package com.adobe.bookstore.order;

import java.util.List;

import com.adobe.bookstore.book.BookStock;
import com.adobe.bookstore.book_order.BookOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderResource {

    private OrderRepository orderRepository;

    @Autowired
    public OrderResource(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    
    @GetMapping("orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return orderRepository.findById(orderId)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("order")
    public String newOrder(@RequestBody List<BookOrder> requestedBooks) {
        Order order = new Order(requestedBooks, true);
        //orderRepository.save(order);
        return requestedBooks.get(0).bookId();
        //return order.getId();
    }
}
