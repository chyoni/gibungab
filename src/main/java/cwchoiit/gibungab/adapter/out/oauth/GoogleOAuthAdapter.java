package cwchoiit.gibungab.adapter.out.oauth;

import cwchoiit.gibungab.application.auth.OAuthUserInfo;
import cwchoiit.gibungab.application.exception.BusinessException;
import cwchoiit.gibungab.application.exception.ErrorCode;
import cwchoiit.gibungab.application.port.out.OAuthPort;
import cwchoiit.gibungab.infrastructure.properties.GoogleOAuthProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component("googleOAuthClient")
public class GoogleOAuthAdapter implements OAuthPort {

    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URI = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final RestClient restClient;

    public GoogleOAuthAdapter(GoogleOAuthProperties properties) {
        this.clientId = properties.clientId();
        this.clientSecret = properties.clientSecret();
        this.redirectUri = properties.redirectUri();
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
                        (req, res) -> { throw BusinessException.of(ErrorCode.OAUTH_TOKEN_EXCHANGE_FAILED, "구글 토큰 교환에 실패했습니다."); })
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
                        (req, res) -> { throw BusinessException.of(ErrorCode.OAUTH_USER_INFO_FAILED, "구글 사용자 정보 조회에 실패했습니다."); })
                .body(Map.class);

        String socialId = (String) response.get("sub");
        String email = (String) response.get("email");
        String nickname = (String) response.get("name");
        String profileImageUrl = (String) response.get("picture");

        return new OAuthUserInfo(socialId, email, nickname, profileImageUrl);
    }
}
