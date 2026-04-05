package cwchoiit.gibungab.application.stats;

import cwchoiit.gibungab.application.port.out.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final ExpenseRepository expenseRepository;

    public MonthlySummary getMonthlySummary(Long memberId, int year, int month) {
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

        List<ExpenseStats> stats = expenseRepository.findMonthlyStats(memberId, from, to);

        BigDecimal totalAmount = stats.stream()
                .map(ExpenseStats::totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalCount = stats.stream().mapToLong(ExpenseStats::count).sum();

        double avgScore = stats.stream()
                .mapToDouble(s -> s.avgScore() * s.count())
                .sum() / Math.max(totalCount, 1);

        List<CategoryStat> categoryStats = stats.stream()
                .map(s -> new CategoryStat(s.categoryId(), s.totalAmount(), s.count(),
                        BigDecimal.valueOf(s.avgScore()).setScale(2, RoundingMode.HALF_UP)))
                .toList();

        return new MonthlySummary(year, month, totalAmount, totalCount,
                BigDecimal.valueOf(avgScore).setScale(2, RoundingMode.HALF_UP), categoryStats);
    }

    public List<EmotionTrend> getEmotionTrend(Long memberId, LocalDate from, LocalDate to) {
        return expenseRepository.findMonthlyEmotionTrend(memberId, from, to);
    }

    public List<CategorySummaryStat> getCategorySummary(Long memberId, LocalDate from, LocalDate to) {
        return expenseRepository.findCategorySummary(memberId, from, to);
    }
}
