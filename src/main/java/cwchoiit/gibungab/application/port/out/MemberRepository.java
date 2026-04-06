package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.domain.member.Member;
import cwchoiit.gibungab.domain.member.SocialProvider;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findBySocialProviderAndSocialId(SocialProvider provider, String socialId);

    Member save(Member member);
}
