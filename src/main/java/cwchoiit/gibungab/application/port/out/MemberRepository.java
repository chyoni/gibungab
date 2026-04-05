package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.domain.member.Member;
import cwchoiit.gibungab.domain.member.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);

    Optional<Member> findByEmail(String email);
}
