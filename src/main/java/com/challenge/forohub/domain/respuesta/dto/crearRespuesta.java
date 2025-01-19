package com.challenge.forohub.domain.respuesta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record crearRespuesta(
    @NotBlank String mensaje,
    @NotNull Long usuarioId,
    @NotNull Long topicoId){
}
