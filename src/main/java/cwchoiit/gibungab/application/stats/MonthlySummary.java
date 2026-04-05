package cwchoiit.gibungab.application.stats;

import java.math.BigDecimal;
import java.util.List;

public record MonthlySummary(int year, int month, BigDecimal totalAmount, long totalCount,
                              BigDecimal avgSatisfactionScore, List<CategoryStat> categoryStats) {
}
