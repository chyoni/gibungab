package cwchoiit.gibungab.adapter.out.security;

import cwchoiit.gibungab.application.port.out.TokenProviderPort;
import cwchoiit.gibungab.infrastructure.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

    private final SecretKey secretKey;
    private final long accessTokenExpiryMs;
    private final long refreshTokenExpiryMs;

    public JwtTokenProviderAdapter(JwtProperties properties) {
        this.secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiryMs = properties.accessTokenExpiryMs();
        this.refreshTokenExpiryMs = properties.refreshTokenExpiryMs();
    }

    @Override
    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessTokenExpiryMs);
    }

    @Override
    public String createRefreshToken(Long memberId) {
        return createToken(memberId, refreshTokenExpiryMs);
    }

    @Override
    public Long getMemberId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    @Override
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public LocalDateTime getRefreshTokenExpiry() {
        return LocalDateTime.now().plusSeconds(refreshTokenExpiryMs / 1000);
    }

    private String createToken(Long memberId, long expiryMs) {
        Date now = new Date();
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiryMs))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
