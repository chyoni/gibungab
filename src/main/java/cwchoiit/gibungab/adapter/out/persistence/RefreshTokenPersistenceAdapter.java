package cwchoiit.gibungab.adapter.out.persistence;

import cwchoiit.gibungab.application.port.out.RefreshTokenRepository;
import cwchoiit.gibungab.domain.auth.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        return jpaRepository.save(refreshToken);
    }

    @Override
    public void delete(RefreshToken refreshToken) {
        jpaRepository.delete(refreshToken);
    }

    @Override
    public void deleteByToken(String token) {
        jpaRepository.deleteByToken(token);
    }
}
