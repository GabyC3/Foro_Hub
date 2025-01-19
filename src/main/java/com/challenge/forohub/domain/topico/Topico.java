package com.challenge.forohub.domain.topico;

import com.challenge.forohub.domain.curso.Curso;
import com.challenge.forohub.domain.topico.dto.actualizarTopico;
import com.challenge.forohub.domain.topico.dto.crearTopico;
import com.challenge.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "topicos")
@Entity(name = "Topico")
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;

    @Column(name="fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name="ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @Enumerated(EnumType.STRING)
    private Estado estado;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    public Topico(crearTopico CrearTopico, Usuario usuario, Curso curso) {
        this.titulo = CrearTopico.titulo();
        this.mensaje = CrearTopico.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
        this.estado = Estado.OPEN;
        this.usuario = usuario;
        this.curso = curso;
    }

    public void actualizarTopicoConCurso(actualizarTopico ActualizarTopico, Curso curso) {
        if (ActualizarTopico.titulo() != null){
            this.titulo = ActualizarTopico.titulo();
        }
        if (ActualizarTopico.mensaje() != null){
            this.mensaje = ActualizarTopico.mensaje();
        }
        if (ActualizarTopico.estado() != null){
            this.estado = ActualizarTopico.estado();
        }
        if (ActualizarTopico.cursoId() != null){
            this.curso = curso;
        }
        this.ultimaActualizacion = LocalDateTime.now();

    }

    public void actualizarTopico(actualizarTopico ActualizarTopico){
        if (ActualizarTopico.titulo() != null){
            this.titulo = ActualizarTopico.titulo();
        }
        if (ActualizarTopico.mensaje() != null){
            this.mensaje = ActualizarTopico.mensaje();
        }
        if(ActualizarTopico.estado() != null){
            this.estado = ActualizarTopico.estado();
        }
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void eliminarTopico(){

        this.estado = Estado.DELETED;
    }

    public void setEstado(Estado estado){

        this.estado = estado;
    }
}
