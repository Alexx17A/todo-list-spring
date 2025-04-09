package com.example.todolistspring.service;
import com.example.todolistspring.models.Tarea;
import com.example.todolistspring.repository.TareaRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> findAll() {
        return this.tareaRepository.findAll();
    }

    public Optional<Tarea> ObtenerPorId(Long id) {
        return this.tareaRepository.findById(id);
    }

    public Tarea Guardar(Tarea tarea) {
        return (Tarea)this.tareaRepository.save(tarea);
    }

    public Tarea Actualizar(Long id, Tarea tareaActualizada) {
        return (Tarea)this.tareaRepository.findById(id).map((tarea) -> {
            tarea.setTitulo(tareaActualizada.getTitulo());
            tarea.setDescripcion(tareaActualizada.getDescripcion());
            tarea.setCompletada(tareaActualizada.getCompletada());
            return (Tarea)this.tareaRepository.save(tarea);
        }).orElseThrow(() -> {
            return new RuntimeException("Tarea no Encontrada");
        });
    }

    public Tarea actualizarParcial(Long id, Map<String, Object> campos) {
        return (Tarea)this.tareaRepository.findById(id).map((tarea) -> {
            if (campos.containsKey("titulo")) {
                tarea.setTitulo((String)campos.get("titulo"));
            }

            if (campos.containsKey("descripcion")) {
                tarea.setDescripcion((String)campos.get("descripcion"));
            }

            if (campos.containsKey("completada")) {
                tarea.setCompletada((Boolean)campos.get("completada"));
            }

            return (Tarea)this.tareaRepository.save(tarea);
        }).orElseThrow(() -> {
            return new RuntimeException("Tarea no Encontrada");
        });
    }

    public void eliminar(Long id) {
        this.tareaRepository.deleteById(id);
    }
}
