package cwchoiit.gibungab.application.category;

import cwchoiit.gibungab.application.port.out.CategoryRepository;
import cwchoiit.gibungab.domain.category.Category;
import cwchoiit.gibungab.infrastructure.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAvailableCategories(Long memberId) {
        return categoryRepository.findAllAvailableByMemberId(memberId);
    }

    @Transactional
    public Category createCustomCategory(Long memberId, String name, String icon) {
        if (categoryRepository.existsByMemberIdAndNameAndDeletedAtIsNull(memberId, name)) {
            throw BusinessException.badRequest("이미 동일한 이름의 카테고리가 존재합니다.");
        }
        return categoryRepository.save(Category.ofCustom(memberId, name, icon));
    }

    @Transactional
    public Category updateCategory(Long memberId, Long categoryId, String name, String icon) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다."));

        if (!category.isCustom() || !category.isOwnedBy(memberId)) {
            throw BusinessException.forbidden("해당 카테고리를 수정할 권한이 없습니다.");
        }

        category.update(name, icon);
        return category;
    }

    @Transactional
    public void deleteCategory(Long memberId, Long categoryId) {
        Category category = categoryRepository.findByIdAndDeletedAtIsNull(categoryId)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다."));

        if (!category.isCustom() || !category.isOwnedBy(memberId)) {
            throw BusinessException.forbidden("해당 카테고리를 삭제할 권한이 없습니다.");
        }

        category.softDelete();
    }
}
