package com.challenge.forohub.controller;

import com.challenge.forohub.domain.respuesta.Respuesta;
import com.challenge.forohub.domain.respuesta.dto.actualizarRespuesta;
import com.challenge.forohub.domain.respuesta.dto.crearRespuesta;
import com.challenge.forohub.domain.respuesta.dto.detalleRespuesta;
import com.challenge.forohub.domain.respuesta.validations.create.validarRespuesta;
import com.challenge.forohub.domain.respuesta.validations.update.validarRespActualizada;
import com.challenge.forohub.domain.topico.Estado;
import com.challenge.forohub.domain.topico.Topico;
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
import com.challenge.forohub.domain.respuesta.repository.respuestaRepository;

import java.util.List;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuesta", description = "Sólo uno puede ser la solución")
public class respuestaController {
    @Autowired
    private topicoRepository topicoRepository;

    @Autowired
    private usuarioRepository usuarioRepository;

    @Autowired
    private respuestaRepository respuestaRepository;

    @Autowired
    List<validarRespuesta> crearValidadores;

    @Autowired
    List<validarRespActualizada> actualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra una nueva respuesta en la base de datos, vinculada a un usuario y tema")
    public ResponseEntity<detalleRespuesta> crearRespuesta(@RequestBody @Valid crearRespuesta crearRespuesta,
                                                           UriComponentsBuilder uriBuilder){
        crearValidadores.forEach(v -> v.validar(crearRespuesta));

        Usuario usuario = usuarioRepository.getReferenceById(crearRespuesta.usuarioId());
        Topico topico = topicoRepository.findById(crearRespuesta.topicoId()).get();

        var respuesta = new Respuesta(crearRespuesta, usuario, topico);
        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new detalleRespuesta(respuesta));

    }

    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Lee todas las respuestas del tema")
    public ResponseEntity<Page<detalleRespuesta>>
    leerRespuestaDeTopico(@PageableDefault(size = 5, sort = {"ultimaActualizacion"},
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long topicoId){
        var pagina = respuestaRepository.findAllByTopicoId(topicoId, pageable).map(detalleRespuesta::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{nombreUsuario}")
    @Operation(summary = "Lee todas las respuestas por nombre de usuario")
    public ResponseEntity<Page<detalleRespuesta>>
    leerRespuestasDeUsuarios(@PageableDefault(size = 5, sort = {"ultimaActualizacion"},
            direction = Sort.Direction.ASC)Pageable pageable, @PathVariable Long usuarioId){
        var pagina = respuestaRepository.findAllByUsuarioId(usuarioId, pageable).map(detalleRespuesta::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lee la única respuesta por su ID")
    public ResponseEntity<detalleRespuesta> leerUnaRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);

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
    @Operation(summary = "Actualiza mensaje, solucion o estado de la respuesta")
    public ResponseEntity<detalleRespuesta> actualizarRespuesta(@RequestBody @Valid actualizarRespuesta actualizarRespuesta, @PathVariable Long id){
        actualizarValidadores.forEach(v -> v.validar(actualizarRespuesta, id));
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.actualizarRespuesta(actualizarRespuesta);

        if(actualizarRespuesta.solucion()){
            var temaResuelto = topicoRepository.getReferenceById(respuesta.getTopico().getId());
            temaResuelto.setEstado(Estado.CLOSED);
        }

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

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina una respuesta por su Id")
    public ResponseEntity<?> borrarRespuesta(@PathVariable Long id){
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.eliminarRespuesta();
        return ResponseEntity.noContent().build();
    }
}
