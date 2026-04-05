package cwchoiit.gibungab.infrastructure.oauth;

import cwchoiit.gibungab.application.auth.OAuthClient;
import cwchoiit.gibungab.application.auth.OAuthUserInfo;
import cwchoiit.gibungab.infrastructure.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component("kakaoOAuthClient")
public class KakaoOAuthClient implements OAuthClient {

    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final String clientId;
    private final String redirectUri;
    private final RestClient restClient;

    public KakaoOAuthClient(
            @Value("${oauth2.kakao.client-id}") String clientId,
            @Value("${oauth2.kakao.redirect-uri}") String redirectUri
    ) {
        this.clientId = clientId;
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
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        Map<String, Object> response = restClient.post()
                .uri(TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .onStatus(status -> status.isError(),
                        (req, res) -> { throw BusinessException.badRequest("카카오 토큰 교환에 실패했습니다."); })
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
                        (req, res) -> { throw BusinessException.badRequest("카카오 사용자 정보 조회에 실패했습니다."); })
                .body(Map.class);

        String socialId = String.valueOf(response.get("id"));
        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        return new OAuthUserInfo(socialId, email, nickname, profileImageUrl);
    }
}
