package cwchoiit.gibungab.application.expense;

import cwchoiit.gibungab.application.exception.BusinessException;
import cwchoiit.gibungab.application.port.in.ExpenseUseCase;
import cwchoiit.gibungab.application.port.out.CategoryRepository;
import cwchoiit.gibungab.application.port.out.ExpenseRepository;
import cwchoiit.gibungab.application.port.out.PageQuery;
import cwchoiit.gibungab.application.port.out.PageResult;
import cwchoiit.gibungab.domain.expense.Emotion;
import cwchoiit.gibungab.domain.expense.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService implements ExpenseUseCase {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Expense create(Long memberId, Long categoryId, BigDecimal amount, String description,
                          String merchant, LocalDate expenseDate,
                          int satisfactionScore, String emotionMemo) {
        validateCategory(memberId, categoryId);
        Emotion emotion = Emotion.of(satisfactionScore, emotionMemo);
        Expense expense = Expense.of(memberId, categoryId, amount, description, merchant, expenseDate, emotion);
        return expenseRepository.save(expense);
    }

    @Override
    public PageResult<Expense> getExpenses(Long memberId, LocalDate from, LocalDate to,
                                            Long categoryId, Integer minScore, Integer maxScore,
                                            PageQuery pageQuery) {
        return expenseRepository.findByMemberIdAndFilters(memberId, from, to, categoryId, minScore, maxScore, pageQuery);
    }

    @Override
    public Expense getExpense(Long memberId, Long expenseId) {
        Expense expense = expenseRepository.findByIdAndNotDeleted(expenseId)
                .orElseThrow(() -> BusinessException.notFound("지출 내역을 찾을 수 없습니다."));

        if (!expense.isOwnedBy(memberId)) {
            throw BusinessException.forbidden("해당 지출 내역에 접근할 권한이 없습니다.");
        }
        return expense;
    }

    @Override
    @Transactional
    public Expense update(Long memberId, Long expenseId, Long categoryId, BigDecimal amount,
                          String description, String merchant, LocalDate expenseDate,
                          int satisfactionScore, String emotionMemo) {
        Expense expense = getExpense(memberId, expenseId);
        validateCategory(memberId, categoryId);

        expense.updateDetails(categoryId, amount, description, merchant, expenseDate);
        expense.changeEmotion(satisfactionScore, emotionMemo);
        return expense;
    }

    @Override
    @Transactional
    public void delete(Long memberId, Long expenseId) {
        Expense expense = getExpense(memberId, expenseId);
        expense.softDelete();
    }

    private void validateCategory(Long memberId, Long categoryId) {
        categoryRepository.findByIdAndNotDeleted(categoryId)
                .filter(c -> !c.isCustom() || c.isOwnedBy(memberId))
                .orElseThrow(() -> BusinessException.badRequest("유효하지 않은 카테고리입니다."));
    }
}
