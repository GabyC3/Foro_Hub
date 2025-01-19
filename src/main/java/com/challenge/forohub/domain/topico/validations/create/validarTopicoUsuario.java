package com.challenge.forohub.domain.topico.validations.create;

import com.challenge.forohub.domain.topico.dto.crearTopico;
import com.challenge.forohub.domain.usuario.repository.usuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validarTopicoUsuario implements validarTopico{

    @Autowired
    private usuarioRepository repository;

    @Override
    public void validar(crearTopico data) {
        var existeUsuario = repository.existsById(data.usuarioId());
        if (!existeUsuario) {
            throw new ValidationException("El usuario no existe");
        }

        var usuarioHabilitado = repository.findById(data.usuarioId()).get().getEnabled();
        if (!usuarioHabilitado) {
            throw new ValidationException("El usuario fue deshabiliado.");
        }
    }
}
