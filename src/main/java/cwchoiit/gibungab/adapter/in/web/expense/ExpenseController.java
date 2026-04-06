package cwchoiit.gibungab.adapter.in.web.expense;

import cwchoiit.gibungab.adapter.in.web.common.ApiResponse;
import cwchoiit.gibungab.application.port.in.ExpenseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseUseCase expenseUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> create(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = ExpenseResponse.from(expenseUseCase.create(
                memberId, request.categoryId(), request.amount(), request.description(),
                request.merchant(), request.expenseDate(),
                request.satisfactionScore(), request.emotionMemo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getExpenses(
            @AuthenticationPrincipal Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(required = false) Integer maxScore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ExpenseResponse> expenses = expenseUseCase
                .getExpenses(memberId, from, to, categoryId, minScore, maxScore, PageRequest.of(page, size))
                .map(ExpenseResponse::from);
        return ResponseEntity.ok(ApiResponse.ok(expenses));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> getExpense(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long expenseId) {
        ExpenseResponse response = ExpenseResponse.from(expenseUseCase.getExpense(memberId, expenseId));
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PatchMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> update(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long expenseId,
            @Valid @RequestBody ExpenseRequest request) {
        ExpenseResponse response = ExpenseResponse.from(expenseUseCase.update(
                memberId, expenseId, request.categoryId(), request.amount(), request.description(),
                request.merchant(), request.expenseDate(),
                request.satisfactionScore(), request.emotionMemo()));
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long expenseId) {
        expenseUseCase.delete(memberId, expenseId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
