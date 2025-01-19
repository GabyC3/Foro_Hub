package com.challenge.forohub.domain.curso;

import com.challenge.forohub.domain.curso.dto.actualizarCurso;
import com.challenge.forohub.domain.curso.dto.crearCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "cursos")
    @Entity(name = "Curso")
    @EqualsAndHashCode(of = "id")

    public class Curso {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;

        @Enumerated(EnumType.STRING)
        private Categoria categoria;
        private Boolean activo;

        public Curso(crearCurso crearCurso) {
            this.name = crearCurso.nombre();
            this.categoria = crearCurso.categoria();
            this.activo = true;
        }

        public void actualizarCurso(actualizarCurso actualizarCurso) {

            if(actualizarCurso.nombre() != null){
                this.name = actualizarCurso.nombre();
            }
            if (actualizarCurso.categoria() != null){
                this.categoria = actualizarCurso.categoria();
            }
            if (actualizarCurso.activo() != null){
                this.activo = actualizarCurso.activo();
            }
        }

        public void eliminarCurso(){
            this.activo = false;
        }
    }

