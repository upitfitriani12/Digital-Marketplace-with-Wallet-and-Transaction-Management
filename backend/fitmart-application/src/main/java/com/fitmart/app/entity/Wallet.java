package com.fitmart.app.entity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer balance;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

}