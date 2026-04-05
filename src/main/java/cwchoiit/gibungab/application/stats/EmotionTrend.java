package cwchoiit.gibungab.application.stats;

import java.math.BigDecimal;

public record EmotionTrend(int year, int month, BigDecimal avgScore, long count) {
}
