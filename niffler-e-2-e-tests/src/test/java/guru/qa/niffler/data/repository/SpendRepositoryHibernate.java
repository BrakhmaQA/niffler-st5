package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;

import java.util.List;

import static guru.qa.niffler.data.DataBase.SPEND;

public class SpendRepositoryHibernate implements SpendRepository {
    private final EntityManager spendEm = EmProvider.entityManager(SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        spendEm.persist(category);
        return category;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        return spendEm.merge(category);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        spendEm.remove(category);
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        spendEm.persist(spend.getCategory());
        spendEm.persist(spend);

        return spend;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        spendEm.merge(spend);

        return spend;
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        spendEm.remove(spend);
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        return List.of(
                spendEm.createQuery("SELECT * FROM \"spend\" where username = :username", SpendEntity.class)
                .setParameter("username", username)
                .getSingleResult());
    }

    @Override
    public CategoryEntity findCategoryByName(String category) {
        return spendEm.createQuery("SELECT * FROM \"category\" WHERE category = :category", CategoryEntity.class)
                .setParameter("category", category)
                .getSingleResult();
    }
}
