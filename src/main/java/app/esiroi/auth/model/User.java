package app.esiroi.auth.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class User implements Serializable {
  @Id private String id;

  private String email;
  private String passwordHash;

  @CreationTimestamp private Instant createdAt;

  private boolean otpValidationRequired;

  @Column(name = "otp_secret")
  private byte[] otpSecret;

  @Transient private String accessToken;
}
