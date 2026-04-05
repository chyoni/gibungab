package cwchoiit.gibungab.domain.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_categories_member_name", columnNames = {"member_id", "name"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 10)
    private String icon;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Category ofPredefined(String name, String icon, int sortOrder) {
        Category category = new Category();
        category.memberId = null;
        category.name = name;
        category.icon = icon;
        category.sortOrder = sortOrder;
        category.createdAt = LocalDateTime.now();
        category.updatedAt = LocalDateTime.now();
        return category;
    }

    public static Category ofCustom(Long memberId, String name, String icon) {
        Category category = new Category();
        category.memberId = memberId;
        category.name = name;
        category.icon = icon;
        category.sortOrder = 0;
        category.createdAt = LocalDateTime.now();
        category.updatedAt = LocalDateTime.now();
        return category;
    }

    public void update(String name, String icon) {
        this.name = name;
        this.icon = icon;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isCustom() {
        return memberId != null;
    }

    public boolean isOwnedBy(Long memberId) {
        return memberId.equals(this.memberId);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
