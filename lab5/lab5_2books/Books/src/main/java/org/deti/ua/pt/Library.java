package org.deti.ua.pt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private final List<Book> store = new ArrayList<>();

    public void addBook(Book book) {
        store.add(book);
    }

    public List<Book> findBooksByAuthor(String author) {
        return store.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> findBooks(LocalDate startDate, LocalDate endDate) {
        return store.stream()
                .filter(book -> book.getPublished().isAfter(startDate) && book.getPublished().isBefore(endDate))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByYear(Integer year) {
        return store.stream()
                .filter(book -> book.getPublished().getYear() == year)
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByTitle(String title) {
        return store.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

}
