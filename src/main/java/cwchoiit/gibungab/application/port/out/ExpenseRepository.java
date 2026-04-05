package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.application.stats.CategorySummaryStat;
import cwchoiit.gibungab.application.stats.EmotionTrend;
import cwchoiit.gibungab.application.stats.ExpenseStats;
import cwchoiit.gibungab.domain.expense.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndDeletedAtIsNull(Long id);

    @Query("""
            SELECT e FROM Expense e
            WHERE e.memberId = :memberId
              AND e.deletedAt IS NULL
              AND e.expenseDate BETWEEN :from AND :to
              AND (:categoryId IS NULL OR e.categoryId = :categoryId)
              AND (:minScore IS NULL OR e.emotion.satisfactionScore >= :minScore)
              AND (:maxScore IS NULL OR e.emotion.satisfactionScore <= :maxScore)
            ORDER BY e.expenseDate DESC, e.id DESC
            """)
    Page<Expense> findByMemberIdAndFilters(
            @Param("memberId") Long memberId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("categoryId") Long categoryId,
            @Param("minScore") Integer minScore,
            @Param("maxScore") Integer maxScore,
            Pageable pageable
    );

    @Query("""
            SELECT new cwchoiit.gibungab.application.stats.ExpenseStats(
                e.categoryId, SUM(e.amount), COUNT(e), AVG(e.emotion.satisfactionScore))
            FROM Expense e
            WHERE e.memberId = :memberId
              AND e.deletedAt IS NULL
              AND e.expenseDate BETWEEN :from AND :to
            GROUP BY e.categoryId
            """)
    List<ExpenseStats> findMonthlyStats(
            @Param("memberId") Long memberId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
            SELECT new cwchoiit.gibungab.application.stats.EmotionTrend(
                YEAR(e.expenseDate), MONTH(e.expenseDate),
                CAST(AVG(e.emotion.satisfactionScore) AS java.math.BigDecimal), COUNT(e))
            FROM Expense e
            WHERE e.memberId = :memberId
              AND e.deletedAt IS NULL
              AND e.expenseDate BETWEEN :from AND :to
            GROUP BY YEAR(e.expenseDate), MONTH(e.expenseDate)
            ORDER BY YEAR(e.expenseDate), MONTH(e.expenseDate)
            """)
    List<EmotionTrend> findMonthlyEmotionTrend(
            @Param("memberId") Long memberId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query("""
            SELECT new cwchoiit.gibungab.application.stats.CategorySummaryStat(
                e.categoryId, SUM(e.amount), COUNT(e),
                CAST(AVG(e.emotion.satisfactionScore) AS java.math.BigDecimal))
            FROM Expense e
            WHERE e.memberId = :memberId
              AND e.deletedAt IS NULL
              AND e.expenseDate BETWEEN :from AND :to
            GROUP BY e.categoryId
            ORDER BY SUM(e.amount) DESC
            """)
    List<CategorySummaryStat> findCategorySummary(
            @Param("memberId") Long memberId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
