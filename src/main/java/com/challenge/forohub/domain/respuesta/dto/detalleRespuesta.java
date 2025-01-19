package com.challenge.forohub.domain.respuesta.dto;

import com.challenge.forohub.domain.respuesta.Respuesta;

import java.time.LocalDateTime;

public record detalleRespuesta(
        Long id,
        String mensaje,

        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion,
        Boolean solucion,
        Boolean borrado,
        Long usuarioId,
        String username,
        Long topicoId,
        String topico
) {
    public detalleRespuesta(Respuesta respuesta){
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getUltimaActualizacion(),
                respuesta.getSolucion(),
                respuesta.getBorrado(),
                respuesta.getUsuario().getId(),
                respuesta.getUsuario().getUsername(),
                respuesta.getTopico().getId(),
                respuesta.getTopico().getTitulo());
    }
}
