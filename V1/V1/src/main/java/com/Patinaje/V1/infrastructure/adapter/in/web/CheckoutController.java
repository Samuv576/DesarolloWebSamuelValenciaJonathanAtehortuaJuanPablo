package com.Patinaje.V1.infrastructure.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Checkout pago", description = "Pantalla de confirmacion de pago")
public class CheckoutController {

    @GetMapping("/checkout_pago")
    @Operation(summary = "Resultado de pago", description = "Muestra estado de pago simulado en el checkout.")
    public String checkoutPago(@RequestParam(name = "status", defaultValue = "pendiente") String status,
                               Model model) {
        boolean exito = "exito".equalsIgnoreCase(status) || "pagado".equalsIgnoreCase(status);
        model.addAttribute("exito", exito);
        model.addAttribute("status", status.toUpperCase());
        return "checkout_pago";
    }
}
