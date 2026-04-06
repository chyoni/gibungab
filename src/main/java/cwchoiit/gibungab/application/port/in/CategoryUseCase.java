package cwchoiit.gibungab.application.port.in;

import cwchoiit.gibungab.domain.category.Category;

import java.util.List;

public interface CategoryUseCase {

    List<Category> getAvailableCategories(Long memberId);

    Category createCustomCategory(Long memberId, String name, String icon);

    Category updateCategory(Long memberId, Long categoryId, String name, String icon);

    void deleteCategory(Long memberId, Long categoryId);
}
