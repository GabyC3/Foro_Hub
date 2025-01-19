package com.challenge.forohub.domain.respuesta.validations.create;

import com.challenge.forohub.domain.respuesta.dto.crearRespuesta;
import com.challenge.forohub.domain.usuario.repository.usuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class respUsuarioValido implements validarRespuesta{

    @Autowired
    private usuarioRepository repository;

    @Override
    public void validar(crearRespuesta data) {
        var usuarioExiste = repository.existsById(data.usuarioId());

        if(!usuarioExiste){
            throw new ValidationException("El usuario no existe");
        }

        var usuarioHabilitado = repository.findById(data.usuarioId()).get().isEnabled();

        if(!usuarioHabilitado){
            throw new ValidationException("El usuario no esta habilitado");
        }
    }
}
