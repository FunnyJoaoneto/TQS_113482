package org.tqs.deti.ua;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StocksPortfolioTest {

    @Mock
    private IStockmarketService stockmarketService;

    @InjectMocks
    private StocksPortfolio portfolio;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTotalValue() {
        when(stockmarketService.lookUpPrice("AAPL")).thenReturn(150.0);
        when(stockmarketService.lookUpPrice("GOOGL")).thenReturn(2800.0);
        lenient().when(stockmarketService.lookUpPrice("MAZD")).thenReturn(20000.0);

        portfolio.addStock(new Stock("AAPL", 2));
        portfolio.addStock(new Stock("GOOGL", 1));

        // assertEquals(3100.0, portfolio.totalValue());
        // assert using Hamcrest
        assertThat(portfolio.totalValue(), is(3100.0));

        verify(stockmarketService).lookUpPrice("AAPL");
        verify(stockmarketService).lookUpPrice("GOOGL");
    }

    @Test
    public void testMostValuableStocks_ReturnsTopN() {
        // Mock stock prices
        when(stockmarketService.lookUpPrice("AAPL")).thenReturn(150.0);
        when(stockmarketService.lookUpPrice("GOOGL")).thenReturn(2800.0);
        when(stockmarketService.lookUpPrice("MSFT")).thenReturn(310.0);
        when(stockmarketService.lookUpPrice("AMZN")).thenReturn(3400.0);

        // Add stocks
        portfolio.addStock(new Stock("AAPL", 2));
        portfolio.addStock(new Stock("GOOGL", 1));
        portfolio.addStock(new Stock("MSFT", 3));
        portfolio.addStock(new Stock("AMZN", 1));

        // Get top 2 valuable stocks
        List<Stock> topStocks = portfolio.mostValuableStocks(2);

        assertEquals(2, topStocks.size());
        assertEquals("AMZN", topStocks.get(0).getLabel());
        assertEquals("GOOGL", topStocks.get(1).getLabel());
    }

    @Test
    public void testMostValuableStocks_TopNGreaterThanStockCount() {
        when(stockmarketService.lookUpPrice(anyString())).thenReturn(100.0);

        portfolio.addStock(new Stock("AAPL", 1));
        portfolio.addStock(new Stock("GOOGL", 1));

        List<Stock> topStocks = portfolio.mostValuableStocks(5);

        assertEquals(2, topStocks.size());
    }

    @Test
    public void testMostValuableStocks_EmptyPortfolio() {
        List<Stock> topStocks = portfolio.mostValuableStocks(3);
        assertTrue(topStocks.isEmpty());
    }

    @Test
    public void testMostValuableStocks_ZeroTopN() {
        lenient().when(stockmarketService.lookUpPrice("AAPL")).thenReturn(150.0);

        portfolio.addStock(new Stock("AAPL", 5));

        List<Stock> topStocks = portfolio.mostValuableStocks(0);

        assertTrue(topStocks.isEmpty());
    }

}