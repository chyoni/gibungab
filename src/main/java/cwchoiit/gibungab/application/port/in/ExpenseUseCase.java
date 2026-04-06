package cwchoiit.gibungab.application.port.in;

import cwchoiit.gibungab.application.port.out.PageQuery;
import cwchoiit.gibungab.application.port.out.PageResult;
import cwchoiit.gibungab.domain.expense.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExpenseUseCase {

    Expense create(Long memberId, Long categoryId, BigDecimal amount, String description,
                   String merchant, LocalDate expenseDate, int satisfactionScore, String emotionMemo);

    PageResult<Expense> getExpenses(Long memberId, LocalDate from, LocalDate to,
                                     Long categoryId, Integer minScore, Integer maxScore, PageQuery pageQuery);

    Expense getExpense(Long memberId, Long expenseId);

    Expense update(Long memberId, Long expenseId, Long categoryId, BigDecimal amount,
                   String description, String merchant, LocalDate expenseDate,
                   int satisfactionScore, String emotionMemo);

    void delete(Long memberId, Long expenseId);
}
