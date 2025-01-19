package com.challenge.forohub.controller;

import com.challenge.forohub.domain.usuario.Usuario;
import com.challenge.forohub.domain.usuario.dto.actualizarUsuario;
import com.challenge.forohub.domain.usuario.dto.crearUsuario;
import com.challenge.forohub.domain.usuario.dto.detalleUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.challenge.forohub.domain.usuario.repository.usuarioRepository;
import com.challenge.forohub.domain.usuario.validations.create.validarCrearUsuario;
import com.challenge.forohub.domain.usuario.validations.update.validarActualizarUsuario;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuario", description = "Crea topicos y publica respuestas")
public class usuarioController {

    @Autowired
    private usuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<validarCrearUsuario> crearValidador;

    @Autowired
    List<validarActualizarUsuario> actualizarValidador;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo usuario en la base de datos")
    public ResponseEntity<detalleUsuario> crearUsuario(@RequestBody @Valid crearUsuario crearUsuario,
                                                       UriComponentsBuilder uriBuilder){
        crearValidador.forEach(v -> v.validar(crearUsuario));

        String hashedPassword = passwordEncoder.encode(crearUsuario.password());
        Usuario usuario = new Usuario(crearUsuario, hashedPassword);

        repository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getUsername()).toUri();
        return ResponseEntity.created(uri).body(new detalleUsuario(usuario));
    }

    @GetMapping("/all")
    @Operation(summary = "Lee a todos los usuarios")
    public ResponseEntity<Page<detalleUsuario>> leerUsuarios(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = repository.findAll(pageable).map(detalleUsuario::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de usuarios habilitados")
    public ResponseEntity<Page<detalleUsuario>> leerUsuariosActivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable){
        var pagina = repository.findAllByEnabledTrue(pageable).map(detalleUsuario::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Lee un usuario por username")
    public ResponseEntity<detalleUsuario> leerUnUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) repository.findByUsername(username);
        var datosUsuario = new detalleUsuario(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Lee un usuario por su ID")
    public ResponseEntity<detalleUsuario>leerUnUsuario(@PathVariable Long id){
        Usuario usuario = repository.getReferenceById(id);
        var datosUsuario = new detalleUsuario(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    @Operation(summary = "Actualiza la contraseña, rol, nombre, apellido, email o estado de un usuario")
    public ResponseEntity<detalleUsuario> actualizarUsuario(@RequestBody @Valid actualizarUsuario actualizarUsuario, @PathVariable String username){
        actualizarValidador.forEach(v -> v.validate(actualizarUsuario));

        Usuario usuario = (Usuario) repository.findByUsername(username);

        if (actualizarUsuario.password() != null){
            String hashedPassword = passwordEncoder.encode(actualizarUsuario.password());
            usuario.actualizarUsuarioConPassword(actualizarUsuario, hashedPassword);

        }else {
            usuario.actualizarUsuario(actualizarUsuario);
        }

        var datosUsuario = new detalleUsuario(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getEnabled()
        );
        return ResponseEntity.ok(datosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    @Operation(summary = "Elimina a un usuario")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) repository.findByUsername(username);
        usuario.eliminarUsuario();
        return ResponseEntity.noContent().build();
    }
}

