package cwchoiit.gibungab.adapter.out.persistence;

import cwchoiit.gibungab.application.port.out.MemberPort;
import cwchoiit.gibungab.domain.member.Member;
import cwchoiit.gibungab.domain.member.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberPort {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Optional<Member> findBySocialProviderAndSocialId(SocialProvider provider, String socialId) {
        return jpaRepository.findBySocialProviderAndSocialId(provider, socialId);
    }

    @Override
    public Member save(Member member) {
        return jpaRepository.save(member);
    }
}
