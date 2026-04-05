package cwchoiit.gibungab.adapter.in.web.category;

import cwchoiit.gibungab.domain.category.Category;

public record CategoryResponse(Long id, String name, String icon, boolean isCustom) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(),
                category.getIcon(), category.isCustom());
    }
}
