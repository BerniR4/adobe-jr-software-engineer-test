package com.adobe.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;

import com.adobe.bookstore.book.BookStock;
import com.adobe.bookstore.book.BookStockRepository;
import com.adobe.bookstore.book_order.BookOrder;
import com.adobe.bookstore.order.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(TestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderResourceTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('11111-11111', 'simple order book', 5)")
    public void shouldPassSimpleOrder() throws JsonProcessingException, InterruptedException {
        BookStock book = new BookStock();
        book.setId("11111-11111");
        List<BookOrder> bookOrders = new LinkedList<>();
        bookOrders.add(new BookOrder(book, 3));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        var result = restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);
        System.out.println("\n\n\n\n" + result + "\n\n\n\n");

        assertThat(result).isNotEqualTo("Order failed: not enough stock");      // Check whether this is a UUID or not

        book = bookStockRepository.findById("11111-11111").orElse(null);

        assertThat(book.getQuantity()).isEqualTo(2);
        System.out.println("\n\n\n\n" + book.getQuantity() + "\n\n\n\n");

    }
}
