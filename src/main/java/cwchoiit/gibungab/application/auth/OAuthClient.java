package cwchoiit.gibungab.application.auth;

public interface OAuthClient {
    OAuthUserInfo getUserInfo(String authorizationCode);
}
