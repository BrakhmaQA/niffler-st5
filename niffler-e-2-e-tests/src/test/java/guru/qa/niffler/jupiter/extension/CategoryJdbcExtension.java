package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;

public class CategoryJdbcExtension extends AbstractCategoryExtension {

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    protected CategoryJson createCategory(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory(category.category());
        categoryEntity.setUsername(category.username());

        return CategoryJson.fromEntity(spendRepository.createCategory(categoryEntity));
    }

    @Override
    protected void removeCategory(CategoryJson category) {
        spendRepository.removeCategory(CategoryEntity.fromJson(category));
    }
}
