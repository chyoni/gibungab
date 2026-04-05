package cwchoiit.gibungab.domain.expense;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "expenses",
        indexes = {
                @Index(name = "idx_expenses_member_date", columnList = "member_id, expense_date"),
                @Index(name = "idx_expenses_member_category", columnList = "member_id, category_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    private String merchant;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Embedded
    private Emotion emotion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Expense of(Long memberId, Long categoryId, BigDecimal amount,
                             String description, String merchant, LocalDate expenseDate,
                             Emotion emotion) {
        Expense expense = new Expense();
        expense.memberId = memberId;
        expense.categoryId = categoryId;
        expense.amount = amount;
        expense.description = description;
        expense.merchant = merchant;
        expense.expenseDate = expenseDate;
        expense.emotion = emotion;
        expense.createdAt = LocalDateTime.now();
        expense.updatedAt = LocalDateTime.now();
        return expense;
    }

    public void updateDetails(Long categoryId, BigDecimal amount, String description,
                              String merchant, LocalDate expenseDate) {
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.merchant = merchant;
        this.expenseDate = expenseDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeEmotion(int satisfactionScore, String memo) {
        this.emotion = Emotion.of(satisfactionScore, memo);
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOwnedBy(Long memberId) {
        return memberId.equals(this.memberId);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
