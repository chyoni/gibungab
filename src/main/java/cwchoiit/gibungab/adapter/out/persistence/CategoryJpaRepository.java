package cwchoiit.gibungab.adapter.out.persistence;

import cwchoiit.gibungab.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL AND (c.memberId IS NULL OR c.memberId = :memberId) ORDER BY c.sortOrder ASC, c.id ASC")
    List<Category> findAllAvailableByMemberId(@Param("memberId") Long memberId);

    Optional<Category> findByIdAndDeletedAtIsNull(Long id);

    boolean existsByMemberIdAndNameAndDeletedAtIsNull(Long memberId, String name);
}
