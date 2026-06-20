package com.fernandoruiz.app.management.model;

import com.fernandoruiz.app.management.model.converter.BooleanToShortConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(length = 50)
    private String email;

    @Convert(converter = BooleanToShortConverter.class)
    @Column(nullable = false, columnDefinition = "smallint")
    @ColumnDefault("0")
    private Boolean locked = false;

    @Convert(converter = BooleanToShortConverter.class)
    @Column(nullable = false, columnDefinition = "smallint")
    @ColumnDefault("0")
    private Boolean disabled = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleEntity> roles = new ArrayList<>();

    // Métodos de conveniencia para manejar roles
    public void addRole(UserRoleEntity role) {
        roles.add(role);
        role.setUser(this);
    }

    public void removeRole(UserRoleEntity role) {
        roles.remove(role);
        role.setUser(null);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", locked=" + locked +
                ", disabled=" + disabled +
                '}';
    }
}