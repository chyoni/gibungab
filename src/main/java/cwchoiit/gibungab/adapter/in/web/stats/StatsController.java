package cwchoiit.gibungab.adapter.in.web.stats;

import cwchoiit.gibungab.application.stats.*;
import cwchoiit.gibungab.infrastructure.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlySummary>> getMonthlySummary(
            @AuthenticationPrincipal Long memberId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.ok(statsService.getMonthlySummary(memberId, year, month)));
    }

    @GetMapping("/emotion-trend")
    public ResponseEntity<ApiResponse<List<EmotionTrend>>> getEmotionTrend(
            @AuthenticationPrincipal Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.ok(statsService.getEmotionTrend(memberId, from, to)));
    }

    @GetMapping("/category-summary")
    public ResponseEntity<ApiResponse<List<CategorySummaryStat>>> getCategorySummary(
            @AuthenticationPrincipal Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.ok(statsService.getCategorySummary(memberId, from, to)));
    }
}
