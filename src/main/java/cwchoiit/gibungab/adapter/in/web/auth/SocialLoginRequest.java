package cwchoiit.gibungab.adapter.in.web.auth;

import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequest(
        @NotBlank(message = "인가 코드는 필수입니다.")
        String authorizationCode
) {
}
