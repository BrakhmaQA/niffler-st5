package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;

public class CategoryJdbcExtension extends AbstractCategoryExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected CategoryJson createCategory(Category category) {
        CategoryJson categoryJson = new CategoryJson(
                null,
                category.category(),
                category.username()
        );

        return categoryJson;
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        CategoryJson categoryJson = new CategoryJson(category.id(), category.category(), category.username());
        spendRepository.removeCategory(CategoryEntity.fromJson(categoryJson));
    }
}
