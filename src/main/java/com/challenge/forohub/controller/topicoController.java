package com.challenge.forohub.controller;

import com.challenge.forohub.domain.curso.Curso;
import com.challenge.forohub.domain.respuesta.Respuesta;
import com.challenge.forohub.domain.respuesta.dto.detalleRespuesta;
import com.challenge.forohub.domain.topico.Estado;
import com.challenge.forohub.domain.topico.Topico;
import com.challenge.forohub.domain.topico.dto.actualizarTopico;
import com.challenge.forohub.domain.topico.dto.crearTopico;
import com.challenge.forohub.domain.topico.dto.detalleTopico;
import com.challenge.forohub.domain.topico.validations.create.validarTopico;
import com.challenge.forohub.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.challenge.forohub.domain.topico.repository.topicoRepository;
import com.challenge.forohub.domain.usuario.repository.usuarioRepository;
import com.challenge.forohub.domain.curso.repository.cursoRepository;
import com.challenge.forohub.domain.respuesta.repository.respuestaRepository;
import com.challenge.forohub.domain.topico.validations.update.validarTopicoActualizado;

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Topic", description = "Está vinculado a un curso y usuario específico")
public class topicoController {

    @Autowired
    private topicoRepository topicoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    cursoRepository cursoRepository;

    @Autowired
   respuestaRepository respuestaRepository;

    @Autowired
    List<validarTopico> crearValidadores;

    @Autowired
    List<validarTopicoActualizado> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo topico en la base de datos")
    public ResponseEntity<detalleTopico> crearTopico(@RequestBody @Valid crearTopico crearTopico,
                                                     UriComponentsBuilder uriBuilder){
        crearValidadores.forEach(v -> v.validar(crearTopico));

        Usuario usuario = usuarioRepository.findById(crearTopico.usuarioId()).get();
        Curso curso = cursoRepository.findById(crearTopico.cursoId()).get();
        Topico topico = new Topico(crearTopico, usuario, curso);

        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new detalleTopico(topico));
    }

    @GetMapping("/all")
    @Operation(summary = "Lee todos los temas")
    public ResponseEntity<Page<detalleTopico>> leerTodosTopicos(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable){
        var pagina = topicoRepository.findAll(pageable).map(detalleTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de temas abiertos y cerrados")
    public ResponseEntity<Page<detalleTopico>> leerTopicosNoEliminados(@PageableDefault(size = 5, sort = {"ultimaActualizacion"}, direction = Sort.Direction.DESC) Pageable pageable){
        var pagina = topicoRepository.findAllByEstadoIsNot(Estado.DELETED, pageable).map(detalleTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lee un tema por su ID")
    public ResponseEntity<detalleTopico> leerUnTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new detalleTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @GetMapping("/{id}/solucion")
    @Operation(summary = "Lee la respuesta del topico como su solución")
    public ResponseEntity<detalleRespuesta> leerSolucionTopico(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceByTopicoId(id);

        var datosRespuesta = new detalleRespuesta(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(datosRespuesta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualiza el título, mensaje, estado o ID del curso de un tema")
    public ResponseEntity<detalleTopico> actualizarTopico(@RequestBody @Valid actualizarTopico actualizarTopicoDTO, @PathVariable Long id){
        actualizarValidadores.forEach(v -> v.validar(actualizarTopicoDTO));

        Topico topico = topicoRepository.getReferenceById(id);

        if(actualizarTopicoDTO.cursoId() != null){
            Curso curso = cursoRepository.getReferenceById(actualizarTopicoDTO.cursoId());
            topico.actualizarTopicoConCurso(actualizarTopicoDTO, curso);
        }else{
            topico.actualizarTopico(actualizarTopicoDTO);
        }

        var datosTopico = new detalleTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(datosTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina un topico")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topico.eliminarTopico();
        return ResponseEntity.noContent().build();
    }
}

