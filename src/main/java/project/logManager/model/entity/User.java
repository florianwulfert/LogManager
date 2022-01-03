package project.logManager.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author - Florian Wulfert
 * 25.11.2021
 **/

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    Integer id;

    @Column(name = "name", unique = true, nullable = false)
    String name;

    @Column(name = "geburtsdatum", nullable = false)
    LocalDate geburtsdatum;

    @Column(name = "gewicht", nullable = false)
    double gewicht;

    @Column(name = "groesse", nullable = false)
    double groesse;

    @Column(name = "lieblingsfarbe", nullable = false)
    String lieblingsfarbe;


}
