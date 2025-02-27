package org.tqs.deti.ua;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductFinderServiceTest {

    @Mock
    private ISimpleHttpClient httpClient; // Mocked HTTP client

    @InjectMocks
    private ProductFinderService productFinderService; // Service under test

    private final String jsonResponse = "{"
            + "\"id\": 3,"
            + "\"title\": \"Mens Cotton Jacket\","
            + "\"price\": 55.99,"
            + "\"description\": \"great outerwear jackets...\","
            + "\"category\": \"men's clothing\","
            + "\"image\": \"https://fakestoreapi.com/img/71li-ujtlUL._AC_UX679_.jpg\""
            + "}";

    @BeforeEach
    void setUp() {
        productFinderService = new ProductFinderService(httpClient);
    }

    @Test
    void testFindProductDetails_ValidId() throws IOException {
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn(jsonResponse);

        Product product = productFinderService.findProductDetails(3).get();
        assertEquals(3, product.getId());
        assertEquals("Mens Cotton Jacket", product.getTitle());
    }

    @Test
    void testFindProductDetails_InvalidId() throws IOException {
        when(httpClient.doHttpGet("https://fakestoreapi.com/products/300")).thenReturn("");

        Optional<Product> product = productFinderService.findProductDetails(300);

        assertTrue(product.isEmpty());
    }
}
