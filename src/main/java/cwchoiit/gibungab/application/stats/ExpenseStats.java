package cwchoiit.gibungab.application.stats;

import java.math.BigDecimal;

public record ExpenseStats(Long categoryId, BigDecimal totalAmount, long count, double avgScore) {
}
