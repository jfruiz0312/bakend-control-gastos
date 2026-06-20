package com.fernandoruiz.app.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity {

    @EmbeddedId
    private UserRoleId id;

    @MapsId("username")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private UserEntity user;

    @Column(name = "role", nullable = false, length = 20, insertable = false, updatable = false)
    private String role;

    @Column(name = "granted_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime grantedDate;

    // Constructor para crear fácilmente
    public UserRoleEntity(UserEntity user, String role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user.getUsername(), role);
    }

    @Override
    public String toString() {
        return "UserRoleEntity{" +
                "username=" + (user != null ? user.getUsername() : null) +
                ", role='" + role + '\'' +
                ", grantedDate=" + grantedDate +
                '}';
    }
}