package com.challenge.forohub.domain.usuario;

import com.challenge.forohub.domain.usuario.dto.actualizarUsuario;
import com.challenge.forohub.domain.usuario.dto.crearUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuarios")
@Entity(name="Usuario")
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails{

    @SuppressWarnings("unused")
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean enabled;

    public Usuario(crearUsuario crearUsuario, String hashedPassword) {
        this.username = crearUsuario.username();
        this.password = hashedPassword;
        this.role = Roles.USUARIO;
        this.nombre = capitalizado(crearUsuario.nombre());
        this.apellido = capitalizado(crearUsuario.apellido());
        this.email = crearUsuario.email();
        this.enabled = true;
    }

    public void actualizarUsuarioConPassword(actualizarUsuario actualizarUsuario, String hashedPassword) {
        if (actualizarUsuario.password() != null){
            this.password = hashedPassword;
        }
        if (actualizarUsuario.roles() != null){
            this.role = actualizarUsuario.roles();
        }
        if (actualizarUsuario.nombre() != null){
            this.nombre = capitalizado(actualizarUsuario.nombre());
        }
        if (actualizarUsuario.apellido() != null){
            this.apellido = capitalizado(actualizarUsuario.apellido());
        }
        if (actualizarUsuario.email() != null){
            this.email = actualizarUsuario.email();
        }
        if (actualizarUsuario.enabled() != null){
            this.enabled = actualizarUsuario.enabled();
        }
    }


    public void actualizarUsuario(actualizarUsuario actualizarUsuario) {
        if (actualizarUsuario.roles() != null){
            this.role = actualizarUsuario.roles();
        }
        if (actualizarUsuario.nombre() != null){
            this.nombre = capitalizado(actualizarUsuario.nombre());
        }
        if (actualizarUsuario.apellido() != null){
            this.apellido = capitalizado(actualizarUsuario.apellido());
        }
        if (actualizarUsuario.email() != null){
            this.email = actualizarUsuario.email();
        }
        if (actualizarUsuario.enabled() != null){
            this.enabled = actualizarUsuario.enabled();
        }
    }

    public void eliminarUsuario(){
        this.enabled = false;
    }

    private String capitalizado(String string) {
        return string.substring(0,1).toUpperCase()+string.substring(1).toLowerCase();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
