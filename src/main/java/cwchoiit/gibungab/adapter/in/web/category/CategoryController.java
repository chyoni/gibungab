package cwchoiit.gibungab.adapter.in.web.category;

import cwchoiit.gibungab.adapter.in.web.common.ApiResponse;
import cwchoiit.gibungab.application.port.in.CategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories(
            @AuthenticationPrincipal Long memberId) {
        List<CategoryResponse> categories = categoryUseCase.getAvailableCategories(memberId)
                .stream()
                .map(CategoryResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = CategoryResponse.from(
                categoryUseCase.createCustomCategory(memberId, request.name(), request.icon()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response));
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = CategoryResponse.from(
                categoryUseCase.updateCategory(memberId, categoryId, request.name(), request.icon()));
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long categoryId) {
        categoryUseCase.deleteCategory(memberId, categoryId);
        return ResponseEntity.ok(ApiResponse.noContent());
    }
}
