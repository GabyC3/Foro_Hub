package com.challenge.forohub.controller;

import com.challenge.forohub.infra.service.tokenService;
import com.challenge.forohub.domain.usuario.Usuario;
import com.challenge.forohub.domain.usuario.dto.autenticacionUsuario;
import com.challenge.forohub.infra.security.dto.JWTtoken;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticación", description = "Obtiene el token del usuario para el acceso.")
public class autenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private tokenService tokenService;

    @PostMapping
    public ResponseEntity<JWTtoken> autenticarUsuario(@RequestBody @Valid autenticacionUsuario autenticacionUsuario){
        Authentication authToken = new UsernamePasswordAuthenticationToken(autenticacionUsuario.username(),
                autenticacionUsuario.password());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());

        return ResponseEntity.ok(new JWTtoken(JWTtoken));
    }

}

