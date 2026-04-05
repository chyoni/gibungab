package cwchoiit.gibungab.adapter.in.web.auth;

import cwchoiit.gibungab.application.auth.AuthService;
import cwchoiit.gibungab.application.auth.TokenPair;
import cwchoiit.gibungab.infrastructure.common.ApiResponse;
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

    private final AuthService authService;

    @PostMapping("/login/kakao")
    public ResponseEntity<ApiResponse<TokenResponse>> loginWithKakao(
            @Valid @RequestBody SocialLoginRequest request) {
        TokenPair tokenPair = authService.loginWithKakao(request.authorizationCode());
        return ResponseEntity.ok(ApiResponse.ok(TokenResponse.from(tokenPair)));
    }

    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse<TokenResponse>> loginWithGoogle(
            @Valid @RequestBody SocialLoginRequest request) {
        TokenPair tokenPair = authService.loginWithGoogle(request.authorizationCode());
        return ResponseEntity.ok(ApiResponse.ok(TokenResponse.from(tokenPair)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshRequest request) {
        TokenPair tokenPair = authService.refresh(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.ok(TokenResponse.from(tokenPair)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
