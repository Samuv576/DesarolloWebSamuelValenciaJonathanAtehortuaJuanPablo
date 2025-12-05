package com.Patinaje.V1.infrastructure.adapter.in.web;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Patinaje.V1.application.usecase.CartService;
import com.Patinaje.V1.domain.model.CartSummary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Carrito y checkout", description = "Operaciones de carrito, envio y simulacion de pago")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add")
    @Operation(summary = "Agregar producto al carrito", description = "Recibe productId y cantidad opcional; almacena por sesion.")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "qty", defaultValue = "1") int qty,
                            HttpSession session) {
        cartService.addItem(session.getId(), productId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/envio")
    @Operation(summary = "Alternar envio", description = "Activa o desactiva el costo de envio en el carrito actual.")
    public String toggleEnvio(@RequestParam(value = "envio", defaultValue = "false") boolean envio, HttpSession session) {
        cartService.setEnvio(session.getId(), envio);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    @Operation(summary = "Vaciar carrito")
    public String clearCart(HttpSession session) {
        cartService.clear(session.getId());
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    @Operation(summary = "Ver carrito", description = "Muestra resumen de items, impuestos y envio.")
    public String viewCart(HttpSession session, Model model) {
        CartSummary summary = cartService.getSummary(session.getId());
        model.addAttribute("summary", summary);
        return "cart";
    }

    @GetMapping("/checkout")
    @Operation(summary = "Checkout", description = "Muestra resumen de pago e informacion de curso si aplica.")
    public String checkout(HttpSession session,
                           @RequestParam(value = "curso", required = false) String curso,
                           @RequestParam(value = "precio", required = false) String precio,
                           @RequestParam(value = "detalle", required = false) String detalle,
                           Model model) {
        CartSummary summary = cartService.getSummary(session.getId());
        model.addAttribute("summary", summary);
        model.addAttribute("cursoSeleccionado", curso);
        model.addAttribute("precioCurso", precio);
        model.addAttribute("detalleCurso", detalle);
        return "checkout";
    }

    @PostMapping("/checkout/pagar")
    @Operation(summary = "Simular pago", description = "Simula resultado de pago y limpia carrito.")
    public String procesarPago(HttpSession session,
                               @RequestParam(name = "estado", defaultValue = "exito") String estado) {
        cartService.clear(session.getId());
        return "redirect:/checkout_pago?status=" + estado;
    }
}
