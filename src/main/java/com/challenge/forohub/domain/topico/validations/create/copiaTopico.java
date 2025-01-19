package com.challenge.forohub.domain.topico.validations.create;

import com.challenge.forohub.domain.topico.repository.topicoRepository;
import com.challenge.forohub.domain.topico.dto.crearTopico;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class copiaTopico implements validarTopico{

    @Autowired
    private topicoRepository topicoRepository;


    @Override
    public void validar(crearTopico data) {
        var CopiaTopico = topicoRepository.existsByTituloAndMensaje(data.titulo(), data.mensaje());
        if(CopiaTopico){
            throw new ValidationException("El topico existe. Revise /topicos/" + topicoRepository.findByTitulo(data.titulo()).getId());

        }
    }
}
