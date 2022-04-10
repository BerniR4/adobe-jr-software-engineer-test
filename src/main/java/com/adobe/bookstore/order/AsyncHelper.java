package com.adobe.bookstore.order;

import java.util.concurrent.Future;

import com.adobe.bookstore.book.BookStock;
import com.adobe.bookstore.book.BookStockRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
public class AsyncHelper {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private BookStockRepository bookStockRepository;

    @Async
    public void updateStock(Order order) throws InterruptedException{

        for (int i = 0; i < order.getAllBookOrders().size(); i++) {
            String bookId = order.getAllBookOrders().get(i).getBookId();
            BookStock bs = bookStockRepository.findById(bookId).orElse(null);

            bs.setQuantity(bs.getQuantity() - order.getAllBookOrders().get(i).getHowMany());
            bookStockRepository.save(bs);
        }

        orderRepository.save(order);
    
    }
}
