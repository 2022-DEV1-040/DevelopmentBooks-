package be.bnp.developmentBooks.controller;

import be.bnp.developmentBooks.dto.Cart;
import be.bnp.developmentBooks.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class BookServicesController {

    private static final Logger logger = LoggerFactory.getLogger(BookServicesController.class);

    @Autowired
    CartService cartService;

    @GetMapping(value = "/ping")
    public ResponseEntity<String> ping() {
        logger.info("Starting services OK .....");
        return new ResponseEntity<String>("Server status : "+ HttpStatus.OK.name(), HttpStatus.OK);
    }

    @GetMapping(value = "/addToCart/{id}")
    public String addToCart(@PathVariable long id) {
        try {
            cartService.add(id);
        } catch (Exception e) {
            logger.error("an exception was thrown during add to cart", e);
            return "Error during add to cart: " + e.getMessage();
        }

        return "Book with id " + id + " added to cart <br/><br/>" + showCart();
    }

    @RequestMapping(value = "/addListToCart", params = "ids", method = RequestMethod.GET)
    public String addListToCart(@RequestParam List<Long> ids) {
        Cart previousCart = new Cart(cartService.getCart());
        for (Long id : ids) {
            try {
                cartService.add(id);
            } catch (Exception e) {
                cartService.setCart(previousCart);
                logger.error("an exception was thrown during add list to cart", e);
                return "Error during add list to cart : " + e.getMessage() + " the previous cart was restored";
            }
        }
        return showCart();
    }

    @GetMapping(value = "/decreaseFromCart/{id}")
    public String decreaseFromCart(@PathVariable long id) {
        try {
            cartService.decrease(id);
        } catch (Exception e) {
            logger.error("an exception was thrown during decreaseFromCart", e);
            return "Error during decrease from cart: " + e.getMessage();
        }
        return "Book with id " + id + " decreased from cart <br/><br/>";
    }

    @GetMapping(value = "/clearCart")
    public String clearCart() {
        cartService.clear();
        return showCart();
    }

    @GetMapping(value = "/showCart")
    public String showCart() {
        return cartService.displayCart();
    }

    @GetMapping(value = "/computeTotalPriceFromCart")
    public String computeTotalPriceFromCart() {
        try {
            return cartService.displayCart() + "Total price : " + String.format("%.2f",cartService.computeTotalPrice()) + "€";
        } catch (Exception e) {
            logger.error("an exception was thrown during computeTotalPriceFromCart", e);
            return "An exception was thrown during computeTotalPriceFromCart see the log";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest req, Exception ex) {
        logger.error("Request: " + req.getRequestURL() + " ERROR " + ex.getStackTrace());

        return ex.getMessage();
    }

    public Cart getCart() {
        return cartService.getCart();
    }
}