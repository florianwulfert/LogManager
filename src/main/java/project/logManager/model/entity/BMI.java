package project.logManager.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="bmi")
public class BMI {
    @Id
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "Alter", nullable = false)
    Integer alter;

    @Column(name = "Groesse", nullable = false)
    Double groesse;

    @Column(name = "Gewicht", nullable = false)
    Double gewicht;

    @Column(name = "BMI", nullable = false)
    Double bmi;

    @Column(name = "weightMessage", nullable = false)
    String weightMessage;

}
