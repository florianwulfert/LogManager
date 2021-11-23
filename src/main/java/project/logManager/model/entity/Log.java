package project.logManager.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="log")
public class Log {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique=true,  nullable = false)
    Integer id;

    @Column(name = "severity", nullable = false)
    String severity;

    @Column(name = "message", nullable = false)
    String message;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;
}
