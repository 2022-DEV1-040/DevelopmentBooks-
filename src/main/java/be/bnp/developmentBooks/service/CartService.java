package be.bnp.developmentBooks.service;


import be.bnp.developmentBooks.dto.Cart;

public interface CartService {

    void add(long id) throws Exception;

    void decrease(long id) throws Exception;

    String displayCart() throws Exception;

    Cart getCart();

    double computeTotalPrice() throws Exception;
}
