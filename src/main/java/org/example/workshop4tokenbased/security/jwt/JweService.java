package org.example.workshop4tokenbased.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.workshop4tokenbased.dto.JwtAuthDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JweService {
    @Value("${app.jws.secret}")
    private String jwsSecret;
    @Value("${app.jwe.secret}")
    private String jweSecret;

    private SecretKey getEncryptionKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jweSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwsSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtAuthDto generateAuthToken(String login) throws JOSEException {
        return JwtAuthDto.builder()
                .token(generateJweToken(login, 2))
                .refreshToken(generateJweToken(login, 15))
                .build();
    }

    public String getLoginFromToken(String token) {
        try {
            SignedJWT signedJWT = parseJweToSignedJWT(token);
            if (signedJWT != null) {
                return signedJWT.getJWTClaimsSet().getSubject();
            }
        } catch (Exception e) {
            log.error("Error extracting login from JWE token", e);
        }
        return null;
    }

    public boolean validateJweToken(String token) {
        try {
            SignedJWT signedJWT = parseJweToSignedJWT(token);
            if (signedJWT != null) {
                if (signedJWT.verify(new MACVerifier(getSigningKey()))) {

                    JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                    Date expirationTime = claimsSet.getExpirationTime();

                    if (expirationTime != null && new Date().after(expirationTime)) {
                        log.error("JWE token expired");
                        return false;
                    }

                    return true;
                } else {
                    log.error("JWS signature verification failed");
                }
            } else {
                log.error("Failed to parse JWE or inner JWS");
            }
        } catch (Exception e) {
            log.error("Error validating JWE token", e);
        }
        return false;
    }

    public JwtAuthDto refreshBaseToken(String login, String refreshToken) throws JOSEException {
        return JwtAuthDto.builder()
                .token(generateJweToken(login, 1))
                .refreshToken(refreshToken)
                .build();
    }

    private String generateJweToken(String login, int expireMinutes) throws JOSEException {

        Date expDate = Date.from(LocalDateTime.now().plusMinutes(expireMinutes)
                .atZone(ZoneId.systemDefault()).toInstant());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(login)
                .expirationTime(expDate)
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(new MACSigner(getSigningKey()));

        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM)
                .contentType("JWT")
                .build();

        SecretKey encryptionKey = getEncryptionKey();
        //Проверка нужна для ввода корректных секретов, алгоритм другие не принимает
        if (encryptionKey.getEncoded().length != 32) {
            throw new IllegalStateException("Encryption key must be 256 bits (32 bytes) for A256GCM");
        }

        JWEObject jweObject = new JWEObject(jweHeader, new Payload(signedJWT));
        jweObject.encrypt(new DirectEncrypter(encryptionKey));

        return jweObject.serialize();
    }

    private SignedJWT parseJweToSignedJWT(String jweString) throws ParseException, JOSEException {
        JWEObject jweObject = JWEObject.parse(jweString);

        SecretKey decryptionKey = getEncryptionKey();
        if (decryptionKey.getEncoded().length != 32) {
            throw new IllegalStateException("Decryption key must be 256 bits (32 bytes) for A256GCM");
        }

        jweObject.decrypt(new DirectDecrypter(decryptionKey));

        if (!"JWT".equals(jweObject.getHeader().getContentType())) {
            log.error("Expected JWE payload to be a JWT");
            return null;
        }

        return SignedJWT.parse(jweObject.getPayload().toString());
    }
}