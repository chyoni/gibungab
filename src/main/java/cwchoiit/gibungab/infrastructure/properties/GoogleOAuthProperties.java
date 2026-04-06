package cwchoiit.gibungab.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.google")
public record GoogleOAuthProperties(
        String clientId,
        String clientSecret,
        String redirectUri
) {
}
