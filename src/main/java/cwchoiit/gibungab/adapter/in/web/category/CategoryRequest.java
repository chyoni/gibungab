package cwchoiit.gibungab.adapter.in.web.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        @Size(max = 50, message = "카테고리 이름은 50자 이하여야 합니다.")
        String name,

        @Size(max = 10, message = "아이콘은 10자 이하여야 합니다.")
        String icon
) {
}
