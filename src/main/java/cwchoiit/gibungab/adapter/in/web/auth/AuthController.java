package cwchoiit.gibungab.adapter.in.web.auth;

import cwchoiit.gibungab.adapter.in.web.common.ApiResponse;
import cwchoiit.gibungab.application.auth.TokenPair;
import cwchoiit.gibungab.application.port.in.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/login/kakao")
    public ResponseEntity<ApiResponse<TokenResponse>> loginWithKakao(
            @Valid @RequestBody SocialLoginRequest request) {
        TokenPair tokenPair = authUseCase.loginWithKakao(request.authorizationCode());
        return ResponseEntity.ok(ApiResponse.ok(TokenResponse.from(tokenPair)));
    }

    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse<TokenResponse>> loginWithGoogle(
            @Valid @RequestBody SocialLoginRequest request) {
        TokenPair tokenPair = authUseCase.loginWithGoogle(request.authorizationCode());
        return ResponseEntity.ok(ApiResponse.ok(TokenResponse.from(tokenPair)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshRequest request) {
        TokenPair tokenPair = authUseCase.refresh(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(TokenResponse.from(tokenPair)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshRequest request) {
        authUseCase.logout(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
