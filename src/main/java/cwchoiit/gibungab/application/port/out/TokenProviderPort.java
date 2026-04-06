package cwchoiit.gibungab.application.port.out;

import java.time.LocalDateTime;

public interface TokenProviderPort {

    String createAccessToken(Long memberId);

    String createRefreshToken(Long memberId);

    Long getMemberId(String token);

    boolean validate(String token);

    LocalDateTime getRefreshTokenExpiry();
}
