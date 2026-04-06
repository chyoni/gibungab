package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.domain.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

    void delete(RefreshToken refreshToken);

    void deleteByToken(String token);
}
