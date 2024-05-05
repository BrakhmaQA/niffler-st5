package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class CategoryExtension implements BeforeEachCallback, AfterEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendRepository spendRepository = SpendRepository.getInstance();

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AnnotationSupport.findAnnotation(
                extensionContext.getRequiredTestMethod(),
                Category.class
        ).ifPresent(
                cat -> {
                    /*CategoryEntity category = new CategoryEntity();
                    category.setCategory(cat.category());
                    category.setUsername(cat.username());

                    spendRepository.createCategory(category);*/

                    CategoryJson category = createCategory(new CategoryJson(
                            null,
                            cat.category(),
                            cat.username()
                    ));

                    spendRepository.createCategory(CategoryEntity.fromJson(category));

                    extensionContext.getStore(NAMESPACE).put(
//                            extensionContext.getUniqueId(), CategoryJson.fromEntity(category)
                            extensionContext.getUniqueId(), category
                    );
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
//        spendRepository.removeCategory(CategoryEntity.fromJson(categoryJson));
        removeCategory(categoryJson);
    }

    protected abstract CategoryJson createCategory(CategoryJson category);

    protected abstract void removeCategory(CategoryJson category);
}
