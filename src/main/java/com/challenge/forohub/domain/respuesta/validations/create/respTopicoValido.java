package com.challenge.forohub.domain.respuesta.validations.create;

import com.challenge.forohub.domain.respuesta.dto.crearRespuesta;
import com.challenge.forohub.domain.topico.Estado;
import com.challenge.forohub.domain.topico.repository.topicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class respTopicoValido implements validarRespuesta{

    @Autowired
    private topicoRepository repository;

    @Override
    public void validar(crearRespuesta data) {
        var topicoExiste = repository.existsById(data.topicoId());

        if (!topicoExiste){
            throw new ValidationException("El topico no existe.");
        }

        var topicoAbierto = repository.findById(data.topicoId()).get().getEstado();

        if(topicoAbierto != Estado.OPEN){
            throw new ValidationException("El topico no esta abierto.");
        }

    }
}
