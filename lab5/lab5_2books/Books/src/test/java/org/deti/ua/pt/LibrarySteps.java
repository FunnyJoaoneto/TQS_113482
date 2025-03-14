package org.deti.ua.pt;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibrarySteps {

    private Library library = new Library();
    private List<Book> searchResults;

    @Given("the following were added to the library:")
    public void the_following_books_were_added(DataTable dataTable) {
        List<Map<String, String>> books = dataTable.asMaps();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map<String, String> row : books) {
            Book book = new Book(
                    row.get("title"),
                    row.get("author"),
                    LocalDate.parse(row.get("published"), formatter));
            library.addBook(book);
        }
    }

    @Given("a book with the title {string}, written by {string}, published in {string}")
    public void addNewBook(final String title, final String author, final String published) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Book book = new Book(title, author, LocalDate.parse(published, formatter));
        library.addBook(book);
    }

    @When("the customer searches for books by author {string}")
    public void the_customer_searches_for_books_by_author(String author) {
        searchResults = library.findBooksByAuthor(author);
    }

    @When("the customer searches for books by title {string}")
    public void the_customer_searches_for_books_by_title(String title) {
        searchResults = library.findBooksByTitle(title);
    }

    @When("the customer searches for books published between {string} and {string}")
    public void the_customer_searches_for_books_published_between(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        searchResults = library.findBooks(start, end);
    }

    @When("the customer searches for books published in {int}")
    public void the_customer_searches_for_books_published_in(Integer year) {
        searchResults = library.findBooksByYear(year);
    }

    @Then("the following books should be found:")
    public void the_following_books_should_be_found(DataTable dataTable) {
        List<Map<String, String>> expectedBooks = dataTable.asMaps();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Book> expectedResults = expectedBooks.stream()
                .map(row -> new Book(row.get("title"), null, LocalDate.parse(row.get("published"), formatter)))
                .collect(Collectors.toList());

        assertEquals(expectedResults.size(), searchResults.size(), "Number of books found does not match");

        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i).getTitle(), searchResults.get(i).getTitle());
            assertEquals(expectedResults.get(i).getPublished(), searchResults.get(i).getPublished());
        }
    }
}
