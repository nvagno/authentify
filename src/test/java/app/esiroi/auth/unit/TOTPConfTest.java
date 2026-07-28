package app.esiroi.auth.unit;

import static org.junit.jupiter.api.Assertions.*;

import app.esiroi.auth.endpoint.security.TOTPConf;
import org.junit.jupiter.api.Test;

class TOTPConfTest {
  private static final String SECRET_TEST = "JBSWY3DPEHPK3PXP";
  private final TOTPConf totpConf = new TOTPConf();

  @Test
  void generate_totp_uri() {
    String uri = totpConf.getTotpUri(SECRET_TEST, "john.doe@email.com", "MyApp");

    assertEquals(
        "otpauth://totp/MyApp:john.doe@email.com?secret=JBSWY3DPEHPK3PXP&issuer=MyApp&algorithm=SHA1&digits=6&period=30",
        uri);
  }

  @Test
  void generate_same_totp_for_same_counter() {
    String otp1 = totpConf.generateTOTP(SECRET_TEST, 6, 1000L);
    String otp2 = totpConf.generateTOTP(SECRET_TEST, 6, 1000L);

    assertEquals(otp1, otp2);
  }

  @Test
  void generate_different_totp_for_different_counter() {
    String otp1 = totpConf.generateTOTP(SECRET_TEST, 6, 1000L);
    String otp2 = totpConf.generateTOTP(SECRET_TEST, 6, 1001L);

    assertNotEquals(otp1, otp2);
  }

  @Test
  void generate_totp_with_requested_number_of_digits() {
    String otp = totpConf.generateTOTP(SECRET_TEST, 8, 1000L);

    assertEquals(8, otp.length());
  }

  @Test
  void validate_correct_totp() {
    String otp = totpConf.generateTOTP(SECRET_TEST, 6, 30);

    assertTrue(totpConf.validateTOTP(SECRET_TEST, otp));
  }

  @Test
  void reject_invalid_totp() {
    assertFalse(totpConf.validateTOTP(SECRET_TEST, "000000"));
  }
}
