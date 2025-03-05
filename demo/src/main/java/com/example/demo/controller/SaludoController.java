package com.example.demo.controller;

import com.example.demo.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Locale;

@RestController
@RequestMapping("/api/saludos")
public class SaludoController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public Mono<ApiResponse<String>> saludar(@RequestParam(defaultValue = "en") String lang) {
        // Crea un nuevo Locale con el idioma solicitado
        Locale locale = new Locale(lang);

        // Imprime para depuración
        System.out.println("Idioma solicitado: " + lang);
        System.out.println("Locale creado: " + locale);

        String mensaje = messageSource.getMessage("saludo", null, "Default hello", locale);
        String bienvenida = messageSource.getMessage("mensaje.bienvenida", null, "Default welcome", locale);

        // Imprime los mensajes para depuración
        System.out.println("Mensaje obtenido: " + mensaje);
        System.out.println("Bienvenida obtenida: " + bienvenida);

        return Mono.just(ApiResponse.success(mensaje, bienvenida));
    }
}