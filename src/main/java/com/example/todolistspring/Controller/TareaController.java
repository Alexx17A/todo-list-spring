package com.example.todolistspring.Controller;

import com.example.todolistspring.models.Tarea;
import com.example.todolistspring.security.JwtUtil;
import com.example.todolistspring.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


// hola
@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final JwtUtil jwtUtil;

    @Autowired // Opcional (Spring lo inyecta igual, pero es buena práctica)
    public TareaController(TareaService tareaService, JwtUtil jwtUtil) {
        this.tareaService = tareaService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/hola")  // Ruta final: GET /api/tareas/hola
    public String holaMundo() {
        return "Mire profe si jalo a la primera jeje";
    }
/*
    @GetMapping // Ruta final: GET /api/tareas
    public List<Tarea> obtenerTodas() {
        return tareaService.findAll();
    }

 */

    @GetMapping("/{id}") // Ruta final: GET /api/tareas/1
    public ResponseEntity<Tarea> obtenerPorId(@PathVariable long id) {
        Optional<Tarea> tarea = tareaService.ObtenerPorId(id);
        return tarea.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping // Ruta final: POST /api/tareas
    public Tarea crear(@RequestBody Tarea tarea) {
        return tareaService.Guardar(tarea);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable Long id, @RequestBody Tarea tareaActualizada) {
        try {
            Tarea tarea = tareaService.Actualizar(id, tareaActualizada);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarea> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        try {
            Tarea tarea = tareaService.actualizarParcial(id, campos);
            return ResponseEntity.ok(tarea);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        if (tareaService.ObtenerPorId(id).isPresent()) {
            tareaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Tarea> obtenerTodas(@RequestHeader("Authorization") String token) {
        String username = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));

        // Obtener todas las tareas y filtrar por usuario
        return tareaService.findAll().stream()
                .filter(tarea -> tarea.getUsuario() != null &&
                        tarea.getUsuario().getUsername().equals(username))
                .collect(Collectors.toList());
    }
}




