package org.tqs.deti.ua;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductFinderServiceIT {

    private TqsBasicHttpClient httpClient;
    private ProductFinderService productFinderService;

    @BeforeEach
    public void setUp() {
        httpClient = new TqsBasicHttpClient();
        productFinderService = new ProductFinderService(httpClient);
    }

    @Test
    public void testFindProductDetails_ValidId() throws IOException {
        Product product = productFinderService.findProductDetails(3).get();
        assertEquals(3, product.getId());
        assertEquals("Mens Cotton Jacket", product.getTitle());
    }

    @Test
    public void testFindProductDetails_InvalidId() throws IOException {
        Optional<Product> response = productFinderService.findProductDetails(300);
        assertTrue(response.isEmpty());
    }
}
