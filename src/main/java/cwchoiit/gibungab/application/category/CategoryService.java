package cwchoiit.gibungab.application.category;

import cwchoiit.gibungab.application.exception.BusinessException;
import cwchoiit.gibungab.application.port.in.CategoryUseCase;
import cwchoiit.gibungab.application.port.out.CategoryPort;
import cwchoiit.gibungab.domain.category.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService implements CategoryUseCase {

    private final CategoryPort categoryPort;

    @Override
    public List<Category> getAvailableCategories(Long memberId) {
        return categoryPort.findAllAvailableByMemberId(memberId);
    }

    @Override
    @Transactional
    public Category createCustomCategory(Long memberId, String name, String icon) {
        if (categoryPort.existsByMemberIdAndNameAndNotDeleted(memberId, name)) {
            throw BusinessException.badRequest("이미 동일한 이름의 카테고리가 존재합니다.");
        }
        return categoryPort.save(Category.ofCustom(memberId, name, icon));
    }

    @Override
    @Transactional
    public Category updateCategory(Long memberId, Long categoryId, String name, String icon) {
        Category category = categoryPort.findByIdAndNotDeleted(categoryId)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다."));

        if (!category.isCustom() || !category.isOwnedBy(memberId)) {
            throw BusinessException.forbidden("해당 카테고리를 수정할 권한이 없습니다.");
        }

        category.update(name, icon);
        return category;
    }

    @Override
    @Transactional
    public void deleteCategory(Long memberId, Long categoryId) {
        Category category = categoryPort.findByIdAndNotDeleted(categoryId)
                .orElseThrow(() -> BusinessException.notFound("카테고리를 찾을 수 없습니다."));

        if (!category.isCustom() || !category.isOwnedBy(memberId)) {
            throw BusinessException.forbidden("해당 카테고리를 삭제할 권한이 없습니다.");
        }

        category.softDelete();
    }
}
