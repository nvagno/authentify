package app.esiroi.auth.endpoint.security;

import app.esiroi.auth.model.exception.ApiException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base32;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author Abdul Fatah
 * @date 07/24/24
 * @source
 *     https://blog.stackademic.com/step-by-step-ex-implementing-mfa-with-spring-boot-and-totp-1435588e49ca
 */
@Component
public class TOTPConf {
  private static final int DEFAULT_TOTP_DIGITS = 6;
  private static final int DEFAULT_TOTP_PERIOD = 30; // in seconds

  public String getTotpUri(String secret, String account, String issuer) {
    return String.format(
        "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
        issuer, account, secret, issuer);
  }

  public String generateTOTP(String secret, int digits, int period) {
    long counter = (System.currentTimeMillis() / 1000) / period;
    return generateTOTP(secret, digits, counter);
  }

  @SneakyThrows
  public String generateTOTP(String secret, int digits, long counter) {
    try {
      byte[] keyBytes = new Base32().decode(secret);
      byte[] counterBytes = ByteBuffer.allocate(8).putLong(counter).array();

      SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(keySpec);

      byte[] hash = mac.doFinal(counterBytes);
      int offset = hash[hash.length - 1] & 0xF;
      int binary =
          ((hash[offset] & 0x7F) << 24)
              | ((hash[offset + 1] & 0xFF) << 16)
              | ((hash[offset + 2] & 0xFF) << 8)
              | (hash[offset + 3] & 0xFF);

      int otp = binary % (int) Math.pow(10, digits);
      return StringUtils.leftPad(String.valueOf(otp), digits, '0');

    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new ApiException(e.getMessage(), ApiException.ExceptionType.SERVER_EXCEPTION);
    }
  }

  public boolean validateTOTP(String secret, String otp) {
    String expectedOTP = generateTOTP(secret, DEFAULT_TOTP_DIGITS, DEFAULT_TOTP_PERIOD);
    return expectedOTP.equals(otp);
  }
}
