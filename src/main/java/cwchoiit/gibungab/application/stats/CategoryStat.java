package cwchoiit.gibungab.application.stats;

import java.math.BigDecimal;

public record CategoryStat(Long categoryId, BigDecimal totalAmount, long count, BigDecimal avgScore) {
}
