package cwchoiit.gibungab.application.port.in;

import cwchoiit.gibungab.application.auth.TokenPair;

public interface AuthUseCase {

    TokenPair loginWithKakao(String authorizationCode);

    TokenPair loginWithGoogle(String authorizationCode);

    TokenPair refresh(String refreshToken);

    void logout(String refreshToken);
}
