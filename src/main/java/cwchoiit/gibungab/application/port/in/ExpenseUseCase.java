package cwchoiit.gibungab.application.port.in;

import cwchoiit.gibungab.domain.expense.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseUseCase {

    Expense create(Long memberId, Long categoryId, BigDecimal amount, String description,
                   String merchant, LocalDate expenseDate, int satisfactionScore, String emotionMemo);

    Page<Expense> getExpenses(Long memberId, LocalDate from, LocalDate to,
                               Long categoryId, Integer minScore, Integer maxScore, Pageable pageable);

    Expense getExpense(Long memberId, Long expenseId);

    Expense update(Long memberId, Long expenseId, Long categoryId, BigDecimal amount,
                   String description, String merchant, LocalDate expenseDate,
                   int satisfactionScore, String emotionMemo);

    void delete(Long memberId, Long expenseId);
}
