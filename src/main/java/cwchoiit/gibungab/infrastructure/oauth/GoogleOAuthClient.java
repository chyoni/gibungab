package cwchoiit.gibungab.infrastructure.oauth;

import cwchoiit.gibungab.application.auth.OAuthClient;
import cwchoiit.gibungab.application.auth.OAuthUserInfo;
import cwchoiit.gibungab.infrastructure.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component("googleOAuthClient")
public class GoogleOAuthClient implements OAuthClient {

    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestClient restClient;

    public GoogleOAuthClient(
            @Value("${oauth2.google.client-id}") String clientId,
            @Value("${oauth2.google.client-secret}") String clientSecret,
            @Value("${oauth2.google.redirect-uri}") String redirectUri
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.restClient = RestClient.create();
    }

    @Override
    public OAuthUserInfo getUserInfo(String authorizationCode) {
        String accessToken = exchangeToken(authorizationCode);
        return fetchUserInfo(accessToken);
    }

    @SuppressWarnings("unchecked")
    private String exchangeToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        Map<String, Object> response = restClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .onStatus(status -> status.isError(),
                        (req, res) -> { throw BusinessException.badRequest("구글 토큰 교환에 실패했습니다."); })
                .body(Map.class);

        return (String) response.get("access_token");
    }

    @SuppressWarnings("unchecked")
    private OAuthUserInfo fetchUserInfo(String accessToken) {
        Map<String, Object> response = restClient.get()
                .uri(USER_INFO_URI)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(status -> status.isError(),
                        (req, res) -> { throw BusinessException.badRequest("구글 사용자 정보 조회에 실패했습니다."); })
                .body(Map.class);

        String socialId = (String) response.get("sub");
        String email = (String) response.get("email");
        String nickname = (String) response.get("name");
        String profileImageUrl = (String) response.get("picture");

        return new OAuthUserInfo(socialId, email, nickname, profileImageUrl);
    }
}
