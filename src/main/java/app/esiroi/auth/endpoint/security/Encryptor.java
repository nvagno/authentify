package app.esiroi.auth.endpoint.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;

@Configuration
public class Encryptor {
  private final BytesEncryptor encryptor;

  public Encryptor(
      @Value("${crypto.secret.key}") String cryptoSecretKey,
      @Value("${crypto.salt}") String cryptoSalt) {
    this.encryptor = Encryptors.standard(cryptoSecretKey, cryptoSalt);
  }

  public BytesEncryptor getInstance() {
    return encryptor;
  }
}
