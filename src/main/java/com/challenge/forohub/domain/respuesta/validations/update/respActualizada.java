package com.challenge.forohub.domain.respuesta.validations.update;

import com.challenge.forohub.domain.respuesta.repository.respuestaRepository;
import com.challenge.forohub.domain.topico.repository.topicoRepository;
import com.challenge.forohub.domain.respuesta.Respuesta;
import com.challenge.forohub.domain.respuesta.dto.actualizarRespuesta;
import com.challenge.forohub.domain.topico.Estado;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class respActualizada implements validarRespActualizada {

    @Autowired
    private respuestaRepository respuestaRepository;

    @Autowired
    private topicoRepository topicoRepository;

    @Override
    public void validar(actualizarRespuesta data, Long respuestaId) {
        if (data.solucion()){
            Respuesta respuesta = respuestaRepository.getReferenceById(respuestaId);
            var topicoResuelto = topicoRepository.getReferenceById(respuesta.getTopico().getId());
            if (topicoResuelto.getEstado() == Estado.CLOSED){
                throw new ValidationException("El topico esta solucionado.");
            }
        }
    }
}
