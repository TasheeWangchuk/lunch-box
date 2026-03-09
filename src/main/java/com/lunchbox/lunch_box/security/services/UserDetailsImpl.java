package com.lunchbox.lunch_box.security.services;

import com.lunchbox.lunch_box.modules.user.entity.User;
import com.lunchbox.lunch_box.modules.user.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private String phone;
    @JsonIgnore
    private String password;
    private Boolean active;
    private Collection<? extends GrantedAuthority> authorities;
    // Store full user object for easy access
    @JsonIgnore
    private User user;

    public UserDetailsImpl(Long id, String username, String email, String phone, String password,
            Boolean active,
            Collection<? extends GrantedAuthority> authorities, User user) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.active = active;
        this.authorities = authorities;
        this.user = user;
    }

    public static UserDetailsImpl build(User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getActive(),
                authorities,
                user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return active != null && active;
    }

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return this.id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }

}
