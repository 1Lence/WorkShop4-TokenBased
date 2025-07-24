package org.example.workshop4tokenbased.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banned_token")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BannedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
}