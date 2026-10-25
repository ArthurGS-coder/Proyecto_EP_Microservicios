package com.example.order_service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, List<Map<String, Object>>> payload) {
        List<Map<String, Object>> items = payload.get("items");
        
        // Regla: Rechazar si no hay productos (Error 400)
        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El pedido debe tener al menos un producto"));
        }
        
        // Simulamos creación exitosa (Código 201)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("id", 1, "status", "PENDIENTE", "total", 130.00));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable int id) {
        // Simulamos la consulta del pedido (Código 200)
        return ResponseEntity.ok(Map.of(
            "id", 1, 
            "status", "PENDIENTE", 
            "total", 130.00, 
            "items", Arrays.asList(
                Map.of("productId", 1, "quantity", 2, "unitPrice", 30.00),
                Map.of("productId", 2, "quantity", 1, "unitPrice", 50.00)
            )
        ));
    }
}