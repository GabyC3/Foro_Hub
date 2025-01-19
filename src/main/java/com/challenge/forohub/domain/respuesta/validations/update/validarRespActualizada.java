package com.challenge.forohub.domain.respuesta.validations.update;

import com.challenge.forohub.domain.respuesta.dto.actualizarRespuesta;

public interface validarRespActualizada {
    void validar(actualizarRespuesta data, Long respuestaId);
}
