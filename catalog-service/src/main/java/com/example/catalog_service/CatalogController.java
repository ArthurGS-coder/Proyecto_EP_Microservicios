package com.example.catalog_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @GetMapping
    public List<Map<String, Object>> getCatalog() {
        // Simulamos la respuesta de la base de datos para cumplir rápido con el GET /catalog
        return Arrays.asList(
            Map.of("id", 1, "name", "Producto A", "price", 30.00),
            Map.of("id", 2, "name", "Producto B", "price", 50.00)
        );
    }
}