package project.logManager.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author - EugenFriesen
 * 12.02.2021
 **/

@Data
@Entity
@Table(name="log")
public class Log {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "LogSequence", sequenceName = "log_seq", allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "LogSequence")
    @Column(name = "id", unique=true,  nullable = false)
    Integer id;

    @Column(name = "severity", nullable = false)
    String severity;

    @Column(name = "message", nullable = false)
    String message;

    @Column(name = "timestamp", nullable = false)
    LocalDateTime timestamp;
}
