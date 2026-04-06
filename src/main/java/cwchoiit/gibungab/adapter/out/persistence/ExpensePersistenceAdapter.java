package cwchoiit.gibungab.adapter.out.persistence;

import cwchoiit.gibungab.application.port.out.ExpenseRepository;
import cwchoiit.gibungab.application.port.out.PageQuery;
import cwchoiit.gibungab.application.port.out.PageResult;
import cwchoiit.gibungab.application.stats.CategorySummaryStat;
import cwchoiit.gibungab.application.stats.EmotionTrend;
import cwchoiit.gibungab.application.stats.ExpenseStats;
import cwchoiit.gibungab.domain.expense.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExpensePersistenceAdapter implements ExpenseRepository {

    private final ExpenseJpaRepository jpaRepository;

    @Override
    public Expense save(Expense expense) {
        return jpaRepository.save(expense);
    }

    @Override
    public Optional<Expense> findByIdAndNotDeleted(Long id) {
        return jpaRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public PageResult<Expense> findByMemberIdAndFilters(Long memberId, LocalDate from, LocalDate to,
                                                         Long categoryId, Integer minScore, Integer maxScore,
                                                         PageQuery pageQuery) {
        Page<Expense> page = jpaRepository.findByMemberIdAndFilters(
                memberId, from, to, categoryId, minScore, maxScore,
                PageRequest.of(pageQuery.page(), pageQuery.size())
        );
        return new PageResult<>(
                page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages()
        );
    }

    @Override
    public List<ExpenseStats> findMonthlyStats(Long memberId, LocalDate from, LocalDate to) {
        return jpaRepository.findMonthlyStats(memberId, from, to);
    }

    @Override
    public List<EmotionTrend> findMonthlyEmotionTrend(Long memberId, LocalDate from, LocalDate to) {
        return jpaRepository.findMonthlyEmotionTrend(memberId, from, to);
    }

    @Override
    public List<CategorySummaryStat> findCategorySummary(Long memberId, LocalDate from, LocalDate to) {
        return jpaRepository.findCategorySummary(memberId, from, to);
    }
}
