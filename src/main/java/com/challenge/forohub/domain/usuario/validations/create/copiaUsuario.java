package com.challenge.forohub.domain.usuario.validations.create;

import com.challenge.forohub.domain.usuario.dto.crearUsuario;
import com.challenge.forohub.domain.usuario.repository.usuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class copiaUsuario implements validarCrearUsuario{
    @Autowired
    private usuarioRepository repository;

    @Override
    public void validar(crearUsuario data) {
        var usuarioDuplicado = repository.findByUsername(data.username());
        if(usuarioDuplicado != null){
            throw new ValidationException("El usuario existe");
        }

        var emailDuplicado = repository.findByEmail(data.email());
        if(emailDuplicado != null){
            throw new ValidationException("El email existe");
        }
    }
}
