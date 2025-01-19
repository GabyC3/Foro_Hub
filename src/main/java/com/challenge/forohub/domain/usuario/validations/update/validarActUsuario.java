package com.challenge.forohub.domain.usuario.validations.update;

import com.challenge.forohub.domain.usuario.dto.actualizarUsuario;
import com.challenge.forohub.domain.usuario.repository.usuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validarActUsuario implements validarActualizarUsuario{
    @Autowired
    private usuarioRepository repository;

    @Override
    public void validate(actualizarUsuario data) {
        if(data.email() != null){
            var emailDuplicado = repository.findByEmail(data.email());
            if(emailDuplicado != null){
                throw new ValidationException("Este email esta en uso");
            }
        }
    }
}
