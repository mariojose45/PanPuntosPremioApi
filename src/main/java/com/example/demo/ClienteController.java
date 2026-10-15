
package com.example.demo;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    Conexion conexion = new Conexion();

    @GetMapping("/puntos")
    public Map<String, Object> puntos(@RequestParam String telefono) {

        return conexion.mostrarPuntos(telefono);
    }

    @GetMapping("/canjes")
    public Map<String, Object> canjes(@RequestParam String telefono) {

        return conexion.mostrarCanjes(telefono);
    }
}
