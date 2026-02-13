package com.lunchbox.lunch_box.modules.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = {"provider", "provider_user_id"})
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For LOCAL users you can require it at service layer;
    // for OAuth users you can generate it (or allow null if you want).
    @Size(max = 80)
    @Column(name = "username", length = 80, unique = true)
    private String username;

    // ✅ Must be nullable for Google users
    @Size(max = 225)
    @Column(name = "password_h", length = 225)
    private String password;

    @Size(max = 255)
    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Email
    @Size(max = 180)
    @Column(name = "email", length = 180, unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private AppRole role = AppRole.CUSTOMER;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean active = Boolean.TRUE;

    // ✅ NEW: auth provider
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 20)
    private AuthProvider provider = AuthProvider.LOCAL;

    // ✅ NEW: provider user id ("sub" from Google ID token)
    @Column(name = "provider_user_id", length = 255)
    private String providerUserId;

    // ✅ NEW: useful for Google users
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // last login is NOT creation timestamp
    @UpdateTimestamp
    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;
}
