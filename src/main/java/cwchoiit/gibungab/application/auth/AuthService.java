package cwchoiit.gibungab.application.auth;

import cwchoiit.gibungab.adapter.out.persistence.RefreshTokenRepository;
import cwchoiit.gibungab.application.port.out.MemberRepository;
import cwchoiit.gibungab.domain.auth.RefreshToken;
import cwchoiit.gibungab.domain.member.Member;
import cwchoiit.gibungab.domain.member.SocialProvider;
import cwchoiit.gibungab.infrastructure.common.exception.BusinessException;
import cwchoiit.gibungab.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuthClient kakaoOAuthClient;
    private final OAuthClient googleOAuthClient;

    public AuthService(MemberRepository memberRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtTokenProvider jwtTokenProvider,
                       @Qualifier("kakaoOAuthClient") OAuthClient kakaoOAuthClient,
                       @Qualifier("googleOAuthClient") OAuthClient googleOAuthClient) {
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoOAuthClient = kakaoOAuthClient;
        this.googleOAuthClient = googleOAuthClient;
    }

    @Transactional
    public TokenPair loginWithKakao(String authorizationCode) {
        OAuthUserInfo userInfo = kakaoOAuthClient.getUserInfo(authorizationCode);
        return processLogin(userInfo, SocialProvider.KAKAO);
    }

    @Transactional
    public TokenPair loginWithGoogle(String authorizationCode) {
        OAuthUserInfo userInfo = googleOAuthClient.getUserInfo(authorizationCode);
        return processLogin(userInfo, SocialProvider.GOOGLE);
    }

    @Transactional
    public TokenPair refresh(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> BusinessException.badRequest("유효하지 않은 Refresh Token입니다."));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw BusinessException.badRequest("만료된 Refresh Token입니다.");
        }

        refreshTokenRepository.delete(refreshToken);
        return issueTokenPair(refreshToken.getMemberId());
    }

    @Transactional
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.deleteByToken(refreshTokenValue);
    }

    private TokenPair processLogin(OAuthUserInfo userInfo, SocialProvider provider) {
        Member member = memberRepository
                .findBySocialProviderAndSocialId(provider, userInfo.socialId())
                .orElseGet(() -> memberRepository.save(
                        Member.of(userInfo.email(), userInfo.nickname(),
                                userInfo.profileImageUrl(), provider, userInfo.socialId())
                ));

        if (!member.getNickname().equals(userInfo.nickname())) {
            member.updateProfile(userInfo.nickname(), userInfo.profileImageUrl());
        }

        return issueTokenPair(member.getId());
    }

    private TokenPair issueTokenPair(Long memberId) {
        String accessToken = jwtTokenProvider.createAccessToken(memberId);
        String refreshTokenValue = jwtTokenProvider.createRefreshToken(memberId);

        RefreshToken refreshToken = RefreshToken.of(memberId, refreshTokenValue,
                jwtTokenProvider.getRefreshTokenExpiry());
        refreshTokenRepository.save(refreshToken);

        return new TokenPair(accessToken, refreshTokenValue);
    }
}
