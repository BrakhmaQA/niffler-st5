package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private static final DataSource spendDataSource = DataSourceProvider.dataSource(DataBase.SPEND);

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO \"category\" (category, username) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.executeUpdate();

            UUID generatedId;

            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can't access to id");
                }
            }

            category.setId(generatedId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public CategoryEntity editCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE \"category\" SET category = ?, username = ? WHERE category = ?"
             )) {
            ps.setString(1, category.getCategory());
            ps.setString(2, category.getUsername());
            ps.setObject(3, category.getCategory());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return category;
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM \"category\" WHERE id = ?"
             )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SpendEntity createSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement categoryPs = conn.prepareStatement(
                     "INSERT INTO \"category\" (category, username) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement spendPs = conn.prepareStatement(
                     "INSERT INTO \"spend\" (username, spend_date, currency, amount, description, category_id) VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            categoryPs.setString(1, spend.getCategory().getCategory());
            categoryPs.setString(2, spend.getCategory().getUsername());
            categoryPs.executeUpdate();

            UUID categoryId;
            try (ResultSet keys = categoryPs.getGeneratedKeys()) {
                if (keys.next()) {
                    categoryId = UUID.fromString(keys.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t find id");
                }
            }

            CategoryEntity category = new CategoryEntity();
            category.setId(categoryId);
            category.setCategory(spend.getCategory().getCategory());
            category.setUsername(spend.getCategory().getUsername());

            spendPs.setString(1, spend.getUsername());
            spendPs.setDate(2, new Date(System.currentTimeMillis()));
            spendPs.setString(3, String.valueOf(spend.getCurrency()));
            spendPs.setDouble(4, spend.getAmount());
            spendPs.setString(5, spend.getDescription());
            spendPs.setObject(6, categoryId);
            spendPs.executeUpdate();

            UUID spendId;

            try (ResultSet keys = spendPs.getGeneratedKeys()) {
                if (keys.next()) {
                    spendId = UUID.fromString(keys.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t find id");
                }
            }

            spend.setId(spendId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return spend;
    }

    @Override
    public SpendEntity editSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE \"spend\" SET username = ?, currency = ?, " +
                     "spend_date = ?, amount = ?, description = ?, category_id = ? WHERE id = ?")) {
            ps.setString(1, spend.getUsername());
            ps.setString(2, String.valueOf(spend.getCurrency()));
            ps.setDate(3, new Date(System.currentTimeMillis()));
            ps.setDouble(4, spend.getAmount());
            ps.setString(5, spend.getDescription());
            ps.setObject(6, spend.getCategory().getId());
            ps.setObject(7, spend.getId());
            ps.executeUpdate();
            return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeSpend(SpendEntity spend) {
        try (Connection conn = spendDataSource.getConnection();
             PreparedStatement spendPs = conn.prepareStatement(
                     "DELETE FROM \"spend\" WHERE id = ?");
             PreparedStatement categoryPs = conn.prepareStatement(
                     "DELETE FROM \"category\" WHERE id = ?")
        ) {
            spendPs.setObject(1, spend.getId());
            spendPs.executeUpdate();

            categoryPs.setObject(1, spend.getCategory().getId());
            categoryPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
