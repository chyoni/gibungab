package cwchoiit.gibungab.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.kakao")
public record KakaoOAuthProperties(
        String clientId,
        String redirectUri
) {
}
