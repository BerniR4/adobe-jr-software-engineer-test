package com.adobe.bookstore.book_order;

import java.io.Serializable;
import java.util.Objects;

public class BookOrderId implements Serializable{
    private String book;
    private String order;

    @Override
    public boolean equals(Object o) {

        if (this == o)
        return true;
        if (!(o instanceof BookOrderId))
        return false;
        BookOrderId employee = (BookOrderId) o;
        return Objects.equals(this.book, employee.book) && Objects.equals(this.order, employee.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.book, this.order);
    }
}
