package project.logManager.model.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author - EugenFriesen 12.02.2021
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "log")
public class Log {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user")
  User user;

  @Column(name = "severity", nullable = false)
  String severity;

  @Column(name = "message", nullable = false)
  String message;

  @Column(name = "timestamp", nullable = false)
  LocalDateTime timestamp;
}
