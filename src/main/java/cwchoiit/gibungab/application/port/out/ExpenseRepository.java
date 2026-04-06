package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.application.stats.CategorySummaryStat;
import cwchoiit.gibungab.application.stats.EmotionTrend;
import cwchoiit.gibungab.application.stats.ExpenseStats;
import cwchoiit.gibungab.domain.expense.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {

    Expense save(Expense expense);

    Optional<Expense> findByIdAndNotDeleted(Long id);

    Page<Expense> findByMemberIdAndFilters(Long memberId, LocalDate from, LocalDate to,
                                            Long categoryId, Integer minScore, Integer maxScore,
                                            Pageable pageable);

    List<ExpenseStats> findMonthlyStats(Long memberId, LocalDate from, LocalDate to);

    List<EmotionTrend> findMonthlyEmotionTrend(Long memberId, LocalDate from, LocalDate to);

    List<CategorySummaryStat> findCategorySummary(Long memberId, LocalDate from, LocalDate to);
}
