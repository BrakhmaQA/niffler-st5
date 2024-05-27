package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;

import java.util.List;

public interface SpendRepository {

    static SpendRepository getInstance() {
        String repo = System.getProperty("repo");

        if ("jdbc".equals(repo)) {
            return new SpendRepositoryJdbc();
        }
        if ("spring".equals(repo)) {
            return new SpendRepositoryJdbc();
        }

        return new SpendRepositoryJdbc();
    }

    CategoryEntity createCategory(CategoryEntity category);

    CategoryEntity editCategory(CategoryEntity category);

    void removeCategory(CategoryEntity category);

    SpendEntity createSpend(SpendEntity spend);

    SpendEntity editSpend(SpendEntity spend);

    void removeSpend(SpendEntity spend);

    List<SpendEntity> findAllByUsername(String username);
}
