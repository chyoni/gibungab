package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.application.stats.CategorySummaryStat;
import cwchoiit.gibungab.application.stats.EmotionTrend;
import cwchoiit.gibungab.application.stats.ExpenseStats;
import cwchoiit.gibungab.domain.expense.Expense;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpensePort {

    Expense save(Expense expense);

    Optional<Expense> findByIdAndNotDeleted(Long id);

    PageResult<Expense> findByMemberIdAndFilters(Long memberId, LocalDate from, LocalDate to,
                                                  Long categoryId, Integer minScore, Integer maxScore,
                                                  PageQuery pageQuery);

    List<ExpenseStats> findMonthlyStats(Long memberId, LocalDate from, LocalDate to);

    List<EmotionTrend> findMonthlyEmotionTrend(Long memberId, LocalDate from, LocalDate to);

    List<CategorySummaryStat> findCategorySummary(Long memberId, LocalDate from, LocalDate to);
}
