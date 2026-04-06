package cwchoiit.gibungab.adapter.out.persistence;

import cwchoiit.gibungab.application.port.out.CategoryPort;
import cwchoiit.gibungab.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements CategoryPort {

    private final CategoryJpaRepository jpaRepository;

    @Override
    public List<Category> findAllAvailableByMemberId(Long memberId) {
        return jpaRepository.findAllAvailableByMemberId(memberId);
    }

    @Override
    public Optional<Category> findByIdAndNotDeleted(Long id) {
        return jpaRepository.findByIdAndDeletedAtIsNull(id);
    }

    @Override
    public boolean existsByMemberIdAndNameAndNotDeleted(Long memberId, String name) {
        return jpaRepository.existsByMemberIdAndNameAndDeletedAtIsNull(memberId, name);
    }

    @Override
    public Category save(Category category) {
        return jpaRepository.save(category);
    }
}
