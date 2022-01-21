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

    @Column(name = "birthdate", nullable = false)
    LocalDate birthdate;

    @Column(name = "weight", nullable = false)
    double weight;

    @Column(name = "height", nullable = false)
    double height;

    @Column(name = "favouriteColor", nullable = false)
    String favouriteColor;

    @Column(name = "bmi")
    double bmi;


}
