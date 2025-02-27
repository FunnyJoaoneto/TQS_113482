package org.tqs.deti.ua;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StocksPortfolio {
    private IStockmarketService stockmarket;
    private List<Stock> stocks;

    public StocksPortfolio(IStockmarketService stockmarket) {
        this.stockmarket = stockmarket;
        this.stocks = new ArrayList<>();
    }

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public double totalValue() {
        double total = 0;
        for (Stock stock : stocks) {
            total += stock.getQuantity() * stockmarket.lookUpPrice(stock.getLabel());
        }
        return total;
    }

    /**
     * Returns the top N most valuable stocks in the portfolio.
     * 
     * @param topN number of stocks to return
     * @return list of top N most valuable stocks
     */
    public List<Stock> mostValuableStocks(int topN) {
        return stocks.stream()
                .sorted((s1, s2) -> Double.compare(
                        s2.getQuantity() * stockmarket.lookUpPrice(s2.getLabel()),
                        s1.getQuantity() * stockmarket.lookUpPrice(s1.getLabel())))
                .limit(topN)
                .collect(Collectors.toList());
    }
}
