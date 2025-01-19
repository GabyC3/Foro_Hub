package com.challenge.forohub.domain.topico.validations.update;

import com.challenge.forohub.domain.curso.repository.cursoRepository;
import com.challenge.forohub.domain.topico.dto.actualizarTopico;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class validarCursoActualizado implements validarTopicoActualizado{

    @Autowired
    private cursoRepository repository;

    @Override
    public void validar(actualizarTopico data) {
        if(data.cursoId() != null){
            var ExisteCurso = repository.existsById(data.cursoId());
            if (!ExisteCurso){
                throw new ValidationException("El curso no existe");
            }

            var cursoHabilitado = repository.findById(data.cursoId()).get().getActivo();
            if(!cursoHabilitado){
                throw new ValidationException("El curso no esta disponible en este momento.");
            }
        }

    }
}
