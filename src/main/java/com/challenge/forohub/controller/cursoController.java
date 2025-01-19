package com.challenge.forohub.controller;

import com.challenge.forohub.domain.curso.Curso;
import com.challenge.forohub.domain.curso.dto.actualizarCurso;
import com.challenge.forohub.domain.curso.dto.crearCurso;
import com.challenge.forohub.domain.curso.dto.detalleCurso;
import com.challenge.forohub.domain.curso.repository.cursoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Curso", description = "Puede pertenecer a una de las categorías")
public class cursoController {

    @Autowired
    private cursoRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar un nuevo curso en la BD")
    public ResponseEntity<detalleCurso> crearTopico(@RequestBody @Valid crearCurso crearCurso,
                                                    UriComponentsBuilder uriBuilder){
        Curso curso = new Curso(crearCurso);
        repository.save(curso);
        var uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new detalleCurso(curso));

    }

    @GetMapping("/all")
    @Operation(summary = "Lee todos los cursos")
    public ResponseEntity<Page<detalleCurso>> listarCursos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = repository.findAll(pageable).map(detalleCurso::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de cursos activos")
    public ResponseEntity<Page<detalleCurso>> listarCursosActivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = repository.findAllByActivoTrue(pageable).map(detalleCurso::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lee un solo curso por su ID")
    public ResponseEntity<detalleCurso> ListarUnCurso(@PathVariable Long id){
        Curso curso = repository.getReferenceById(id);
        var datosDelCurso = new detalleCurso(
                curso.getId(),
                curso.getName(),
                curso.getCategoria(),
                curso.getActivo()
        );
        return ResponseEntity.ok(datosDelCurso);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza nombre, categoría o estado de un curso")
    public ResponseEntity<detalleCurso> actualizarCurso(@RequestBody @Valid actualizarCurso actualizarCurso, @PathVariable Long id){

        Curso curso = repository.getReferenceById(id);
        curso.actualizarCurso(actualizarCurso);

        var datosDelCurso = new detalleCurso(
                curso.getId(),
                curso.getName(),
                curso.getCategoria(),
                curso.getActivo()
        );
        return ResponseEntity.ok(datosDelCurso);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un curso")
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id){
        Curso curso = repository.getReferenceById(id);
        curso.eliminarCurso();
        return ResponseEntity.noContent().build();
    }

}

