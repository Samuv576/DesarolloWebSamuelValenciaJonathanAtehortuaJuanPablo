package com.Patinaje.V1.infrastructure.adapter.in.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.application.usecase.CartService;
import com.Patinaje.V1.domain.model.CartSummary;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "qty", defaultValue = "1") int qty,
                            HttpSession session) {
        cartService.addItem(session.getId(), productId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/envio")
    public String toggleEnvio(@RequestParam(value = "envio", defaultValue = "false") boolean envio, HttpSession session) {
        cartService.setEnvio(session.getId(), envio);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        cartService.clear(session.getId());
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        CartSummary summary = cartService.getSummary(session.getId());
        model.addAttribute("summary", summary);
        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        CartSummary summary = cartService.getSummary(session.getId());
        model.addAttribute("summary", summary);
        return "checkout";
    }
}
