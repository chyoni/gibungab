package cwchoiit.gibungab.adapter.out.persistence;

import cwchoiit.gibungab.domain.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByMemberId(Long memberId);

    void deleteByToken(String token);
}
