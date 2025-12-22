package app.esiroi.auth.model.dto;

import java.io.Serializable;
import java.time.Instant;
import lombok.Data;

@Data
public class MfaChallenge implements Serializable {
  private String challengeId;
  private String userId;
  private int attempts;
  private Instant createdAt;
}
