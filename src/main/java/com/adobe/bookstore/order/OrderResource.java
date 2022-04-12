package com.adobe.bookstore.order;

import java.util.List;

import com.adobe.bookstore.book.BookStock;
import com.adobe.bookstore.book.BookStockRepository;
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
    private BookStockRepository bookStockRepository;

    @Autowired
    private AsyncHelper asyncHelper;

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
        fixDuplicate(requestedBooks);

        if (!checkStock(requestedBooks))
            return "Order failed";
        
        Order order = new Order(requestedBooks, false);
        for (BookOrder bookOrder : requestedBooks) {
            bookOrder.setOrder(order);
        }
        try {
            asyncHelper.updateStock(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return order.getId();
    }

    private boolean checkStock(List<BookOrder> requestedBoooks) {
        for (BookOrder bookOrder : requestedBoooks) {
            BookStock bs = bookStockRepository.findById(bookOrder.getBookId()).orElse(null);
            if (null == bs) 
                return false;
            if (bookOrder.getHowMany() > bs.getQuantity() || bookOrder.getHowMany() < 0)
                return false;
        }
        return true;
    }

    private void fixDuplicate(List<BookOrder> requestedBooks) {

        for (int i = 0; i < requestedBooks.size(); i++) {
            int j = i + 1;
            while (j < requestedBooks.size()) {
                if (requestedBooks.get(i).getBookId().equals(requestedBooks.get(j).getBookId())) {
                    requestedBooks.get(i).addHowMany(requestedBooks.get(j).getHowMany());
                    requestedBooks.remove(j);
                } else {
                    j++;
                }
            }
        }
    }

}
