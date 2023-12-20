package org.itstep.auth_project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    private String role;

    private String description;

    public Role(Long id, String role_user, String user_role) {
        this.setId(id);
        this.role = role_user;
        this.description = user_role;
    }

    @Override public String getAuthority() {
        return getRole();
    }
}