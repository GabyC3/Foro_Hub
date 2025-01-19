package com.challenge.forohub.domain.topico.dto;

import com.challenge.forohub.domain.curso.Categoria;
import com.challenge.forohub.domain.topico.Estado;
import com.challenge.forohub.domain.topico.Topico;

import java.time.LocalDateTime;

public record detalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        LocalDateTime ultimaActualizacion,
        Estado estado,
        String usuario,
        String curso,
        Categoria categoriaCurso

) {

    public detalleTopico(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getUltimaActualizacion(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
    }

}
