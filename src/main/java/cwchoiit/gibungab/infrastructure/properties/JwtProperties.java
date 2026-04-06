package cwchoiit.gibungab.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpiryMs,
        long refreshTokenExpiryMs
) {
}
