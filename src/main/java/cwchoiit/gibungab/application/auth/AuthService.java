package cwchoiit.gibungab.application.auth;

import cwchoiit.gibungab.application.exception.BusinessException;
import cwchoiit.gibungab.application.exception.ErrorCode;
import cwchoiit.gibungab.application.port.in.AuthUseCase;
import cwchoiit.gibungab.application.port.out.MemberPort;
import cwchoiit.gibungab.application.port.out.OAuthPort;
import cwchoiit.gibungab.application.port.out.RefreshTokenPort;
import cwchoiit.gibungab.application.port.out.TokenProviderPort;
import cwchoiit.gibungab.domain.auth.RefreshToken;
import cwchoiit.gibungab.domain.member.Member;
import cwchoiit.gibungab.domain.member.SocialProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService implements AuthUseCase {

    private final MemberPort memberPort;
    private final RefreshTokenPort refreshTokenPort;
    private final TokenProviderPort tokenProvider;
    private final OAuthPort kakaoOAuthClient;
    private final OAuthPort googleOAuthClient;

    public AuthService(MemberPort memberPort,
                       RefreshTokenPort refreshTokenPort,
                       TokenProviderPort tokenProvider,
                       @Qualifier("kakaoOAuthClient") OAuthPort kakaoOAuthClient,
                       @Qualifier("googleOAuthClient") OAuthPort googleOAuthClient) {
        this.memberPort = memberPort;
        this.refreshTokenPort = refreshTokenPort;
        this.tokenProvider = tokenProvider;
        this.kakaoOAuthClient = kakaoOAuthClient;
        this.googleOAuthClient = googleOAuthClient;
    }

    @Override
    @Transactional
    public TokenPair loginWithKakao(String authorizationCode) {
        OAuthUserInfo userInfo = kakaoOAuthClient.getUserInfo(authorizationCode);
        return processLogin(userInfo, SocialProvider.KAKAO);
    }

    @Override
    @Transactional
    public TokenPair loginWithGoogle(String authorizationCode) {
        OAuthUserInfo userInfo = googleOAuthClient.getUserInfo(authorizationCode);
        return processLogin(userInfo, SocialProvider.GOOGLE);
    }

    @Override
    @Transactional
    public TokenPair refresh(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenPort.findByToken(refreshTokenValue)
                .orElseThrow(() -> BusinessException.of(ErrorCode.INVALID_REFRESH_TOKEN, "유효하지 않은 Refresh Token입니다."));

        if (refreshToken.isExpired()) {
            refreshTokenPort.delete(refreshToken);
            throw BusinessException.of(ErrorCode.EXPIRED_REFRESH_TOKEN, "만료된 Refresh Token입니다.");
        }

        refreshTokenPort.delete(refreshToken);
        return issueTokenPair(refreshToken.getMemberId());
    }

    @Override
    @Transactional
    public void logout(String refreshTokenValue) {
        refreshTokenPort.deleteByToken(refreshTokenValue);
    }

    private TokenPair processLogin(OAuthUserInfo userInfo, SocialProvider provider) {
        Member member = memberPort
                .findBySocialProviderAndSocialId(provider, userInfo.socialId())
                .orElseGet(() -> memberPort.save(
                        Member.of(userInfo.email(), userInfo.nickname(),
                                userInfo.profileImageUrl(), provider, userInfo.socialId())
                ));

        if (!member.getNickname().equals(userInfo.nickname())) {
            member.updateProfile(userInfo.nickname(), userInfo.profileImageUrl());
        }

        return issueTokenPair(member.getId());
    }

    private TokenPair issueTokenPair(Long memberId) {
        String accessToken = tokenProvider.createAccessToken(memberId);
        String refreshTokenValue = tokenProvider.createRefreshToken(memberId);

        RefreshToken refreshToken = RefreshToken.of(memberId, refreshTokenValue,
                tokenProvider.getRefreshTokenExpiry());
        refreshTokenPort.save(refreshToken);

        return new TokenPair(accessToken, refreshTokenValue);
    }
}
