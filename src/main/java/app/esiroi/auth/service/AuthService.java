package app.esiroi.auth.service;

import app.esiroi.auth.endpoint.rest.model.AuthUser;
import app.esiroi.auth.endpoint.security.AuthProvider;
import app.esiroi.auth.endpoint.security.Encryptor;
import app.esiroi.auth.endpoint.security.JWTConf;
import app.esiroi.auth.endpoint.security.TOTPConf;
import app.esiroi.auth.model.User;
import app.esiroi.auth.model.exception.ForbiddenException;
import app.esiroi.auth.model.exception.NotFoundException;
import app.esiroi.auth.repository.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository repository;
  private final PasswordEncoder encoder;
  private final JWTConf jwtConf;
  private final TOTPConf totpConf;
  private final Encryptor encryptor;

  public User getUserByEmail(String email) {
    return repository
        .findByEmail(email)
        .orElseThrow(() -> new NotFoundException("User.email=" + email + " not found"));
  }

  public User authenticateUser(AuthUser toAuthenticate) {
    var user = getUserByEmail(toAuthenticate.getEmail());
    if (encoder.matches(toAuthenticate.getPassword(), user.getPasswordHash())) {
      user.setAccessToken(jwtConf.generateToken(user.getEmail()));
      return user;
    }
    throw new ForbiddenException("Bad credentials");
  }

  public User saveUser(User user) {
    return repository.save(user);
  }

  public User validateOTP(String otp) {
    var email = AuthProvider.getAuthenticatedUserEmail();
    var authUser = getUserByEmail(email);

    var encryptedSecret = authUser.getOtpSecret();
    var decryptedSecret = new String(encryptor.getInstance().decrypt(encryptedSecret));

    var isOTPValid = totpConf.validateTOTP(decryptedSecret, otp);
    if (isOTPValid) {
      authUser.setOtpValidationRequired(false);
      return repository.save(authUser);
    }
    throw new ForbiddenException("OTP Invalid");
  }

  @SneakyThrows
  private String generateQrCode(String otpAuthUrl, int width, int height) {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, width, height);
    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    byte[] pngData = pngOutputStream.toByteArray();
    return Base64.getEncoder().encodeToString(pngData); // Base64 pour HTML <img>
  }

  public String setupTotp(String email) {
    var authUser = getUserByEmail(email);
    var decryptedSecret = new String(encryptor.getInstance().decrypt(authUser.getOtpSecret()));
    String otpAuthUrl = totpConf.getTotpUri(decryptedSecret, authUser.getEmail(), "Authentify");
    return generateQrCode(otpAuthUrl, 200, 200);
  }
}
