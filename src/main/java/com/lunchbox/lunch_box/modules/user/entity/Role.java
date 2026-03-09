package com.lunchbox.lunch_box.modules.user.entity;

import com.lunchbox.lunch_box.modules.user.enums.AppRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true, nullable = false)
    private AppRole name;

    public Role(AppRole name) {
        this.name = name;
    }
}
