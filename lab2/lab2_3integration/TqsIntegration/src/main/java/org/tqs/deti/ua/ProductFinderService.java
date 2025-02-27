package org.tqs.deti.ua;

import java.io.IOException;
import java.util.Optional;

import org.json.JSONObject;

public class ProductFinderService {
    public final String API_PRODUCTS;
    private final ISimpleHttpClient httpClient;

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
        this.API_PRODUCTS = "https://fakestoreapi.com/products";
    }

    public Optional<Product> findProductDetails(Integer id) throws IOException {
        String url = API_PRODUCTS + "/" + id;
        String response = httpClient.doHttpGet(url);
        if (response.isEmpty()) {
            return Optional.empty();
        }
        JSONObject json = new JSONObject(response);
        Product product = new Product(
                json.getInt("id"),
                json.getString("image"),
                json.getString("description"),
                json.getDouble("price"),
                json.getString("title"),
                json.getString("category"));
        return Optional.of(product);

    }
}