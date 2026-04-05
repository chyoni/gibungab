package cwchoiit.gibungab.adapter.in.web.auth;

import cwchoiit.gibungab.application.auth.TokenPair;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse from(TokenPair tokenPair) {
        return new TokenResponse(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
