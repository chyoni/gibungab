package cwchoiit.gibungab.adapter.in.web.expense;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull(message = "카테고리는 필수입니다.")
        Long categoryId,

        @NotNull(message = "금액은 필수입니다.")
        @DecimalMin(value = "0.01", message = "금액은 0보다 커야 합니다.")
        BigDecimal amount,

        @NotBlank(message = "지출 내용은 필수입니다.")
        @Size(max = 200, message = "지출 내용은 200자 이하여야 합니다.")
        String description,

        @Size(max = 100, message = "가맹점명은 100자 이하여야 합니다.")
        String merchant,

        @NotNull(message = "지출 날짜는 필수입니다.")
        LocalDate expenseDate,

        @NotNull(message = "만족도는 필수입니다.")
        @Min(value = 1, message = "만족도는 1 이상이어야 합니다.")
        @Max(value = 5, message = "만족도는 5 이하여야 합니다.")
        Integer satisfactionScore,

        @Size(max = 500, message = "감정 메모는 500자 이하여야 합니다.")
        String emotionMemo
) {
}
