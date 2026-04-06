package cwchoiit.gibungab.application.port.out;

import cwchoiit.gibungab.domain.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    List<Category> findAllAvailableByMemberId(Long memberId);

    Optional<Category> findByIdAndNotDeleted(Long id);

    boolean existsByMemberIdAndNameAndNotDeleted(Long memberId, String name);

    Category save(Category category);
}
