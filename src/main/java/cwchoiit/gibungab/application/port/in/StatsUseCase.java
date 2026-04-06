package cwchoiit.gibungab.application.port.in;

import cwchoiit.gibungab.application.stats.CategorySummaryStat;
import cwchoiit.gibungab.application.stats.EmotionTrend;
import cwchoiit.gibungab.application.stats.MonthlySummary;

import java.time.LocalDate;
import java.util.List;

public interface StatsUseCase {

    MonthlySummary getMonthlySummary(Long memberId, int year, int month);

    List<EmotionTrend> getEmotionTrend(Long memberId, LocalDate from, LocalDate to);

    List<CategorySummaryStat> getCategorySummary(Long memberId, LocalDate from, LocalDate to);
}
