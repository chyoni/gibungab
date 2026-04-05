package cwchoiit.gibungab.adapter.in.web.expense;

import cwchoiit.gibungab.domain.expense.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseResponse(
        Long id,
        Long categoryId,
        BigDecimal amount,
        String description,
        String merchant,
        LocalDate expenseDate,
        int satisfactionScore,
        String emotionMemo,
        LocalDateTime createdAt
) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getCategoryId(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getMerchant(),
                expense.getExpenseDate(),
                expense.getEmotion().getSatisfactionScore(),
                expense.getEmotion().getMemo(),
                expense.getCreatedAt()
        );
    }
}
