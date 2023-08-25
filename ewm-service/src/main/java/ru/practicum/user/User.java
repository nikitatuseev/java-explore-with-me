package ru.practicum.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "email", nullable = false, unique = true)
    String email;
    @Column(name = "name", nullable = false, unique = true)
    String name;
}
