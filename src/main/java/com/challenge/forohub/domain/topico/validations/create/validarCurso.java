package com.challenge.forohub.domain.topico.validations.create;

import com.challenge.forohub.domain.curso.repository.cursoRepository;
import com.challenge.forohub.domain.topico.dto.crearTopico;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validarCurso implements validarTopico{

    @Autowired
    private cursoRepository repository;

    @Override
    public void validar(crearTopico data) {
        var ExisteCurso = repository.existsById(data.cursoId());
        if(!ExisteCurso){
            throw new ValidationException("El curso no existe.");
        }

        var cursoHabilitado = repository.findById(data.cursoId()).get().getActivo();
        if(!cursoHabilitado){
            throw new ValidationException("El curso no esta disponible en este momento.");
        }
    }
}

