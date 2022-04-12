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

        assertThat(result).isNotEqualTo("Order failed");                    // Check whether this is a UUID or not

        book = bookStockRepository.findById("11111-11111").orElse(null);

        assertThat(book.getQuantity()).isEqualTo(2);
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('22222-22222', 'dual order book', 13)")
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('33333-33333', 'dual order book', 4)")
    public void shouldPassDualOrder() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("22222-22222");
        bookOrders.add(new BookOrder(book, 3));

        book = new BookStock();
        book.setId("33333-33333");
        bookOrders.add(new BookOrder(book, 4));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        book = bookStockRepository.findById("22222-22222").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(10);

        book = bookStockRepository.findById("33333-33333").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(0);
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('44444-44444', 'repeataed order book', 10)")
    public void shouldPassRepeatedOrder() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("44444-44444");
        bookOrders.add(new BookOrder(book, 3));

        book = new BookStock();
        book.setId("44444-44444");
        bookOrders.add(new BookOrder(book, 4));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        book = bookStockRepository.findById("44444-44444").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(3);
    }

    @Test
    public void shouldNotPassNegativeOrder() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("44444-44444");
        bookOrders.add(new BookOrder(book, -1));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        var result = restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        assertThat(result).isEqualTo("Order failed");                    // Check whether this is a UUID or not
    }

    @Test
    public void shouldNotPassMissingBook() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("00000-00000");
        bookOrders.add(new BookOrder(book, 3));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        var result = restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        assertThat(result).isEqualTo("Order failed");                    // Check whether this is a UUID or not
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('55555-55555', 'missing stock book order', 3)")
    public void shouldNotPassMissingStock() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("55555-55555");
        bookOrders.add(new BookOrder(book, 4));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        var result = restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        assertThat(result).isEqualTo("Order failed");                    // Check whether this is a UUID or not

        book = bookStockRepository.findById("55555-55555").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(3);
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('66666-66666', 'book 1', 13)")
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('77777-77777', 'book 2', 10)")
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('88888-88888', 'book 3', 5)")
    public void shouldPassCombinedTest() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("66666-66666");
        bookOrders.add(new BookOrder(book, 6));

        book = new BookStock();
        book.setId("77777-77777");
        bookOrders.add(new BookOrder(book, 2));

        book = new BookStock();
        book.setId("88888-88888");
        bookOrders.add(new BookOrder(book, 1));

        book = new BookStock();
        book.setId("66666-66666");
        bookOrders.add(new BookOrder(book, 3));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        book = bookStockRepository.findById("66666-66666").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(4);

        book = bookStockRepository.findById("77777-77777").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(8);

        book = bookStockRepository.findById("88888-88888").orElse(null);
        assertThat(book.getQuantity()).isEqualTo(4);
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('99999-99999', 'book 4', 3)")
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('11111-22222', 'book 5', 4)")
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('11111-33333', 'book 6', 6)")
    public void shouldNotPassCombinedTest() throws JsonProcessingException, InterruptedException {
        List<BookOrder> bookOrders = new LinkedList<>();

        BookStock book = new BookStock();
        book.setId("99999-99999");
        bookOrders.add(new BookOrder(book, 2));

        book = new BookStock();
        book.setId("11111-22222");
        bookOrders.add(new BookOrder(book, 7));

        book = new BookStock();
        book.setId("11111-33333");
        bookOrders.add(new BookOrder(book, 1));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);  
        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(bookOrders), headers);
        
        var result = restTemplate.postForObject("http://localhost:" + port + "/order", request, String.class);

        assertThat(result).isEqualTo("Order failed");                    // Check whether this is a UUID or not
    }
}
