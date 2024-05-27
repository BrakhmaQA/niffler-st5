package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

    private static final DataSource authDataSource = DataSourceProvider.dataSource(DataBase.AUTH);
    private static final DataSource udDataSource = DataSourceProvider.dataSource(DataBase.USERDATA);
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userPs = conn.prepareStatement(
                    "INSERT INTO \"user\" (" +
                            "username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            " VALUES (?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO \"authority\" (" +
                                 "user_id, authority)" +
                                 " VALUES (?, ?)"
                 )) {
                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.executeUpdate();

                UUID generatedUserId;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t access to id");
                    }
                }
                user.setId(generatedUserId);

                for (AuthorityEntity authorityEntity : user.getAuthorities()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, authorityEntity.getAuthority().name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();
                conn.commit();

                return user;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "INSERT INTO \"user\" (" +
                             "username, currency, firstname, surname, photo, photo_small)" +
                             " VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            userPs.executeUpdate();

            UUID generatedUserId;
            try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedUserId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t access to id");
                }
            }
            user.setId(generatedUserId);

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement userPs = conn.prepareStatement(
                    "UPDATE \"user\" SET password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ?" +
                            " WHERE username = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS
            ); PreparedStatement deleteUserAuthorityPs = conn.prepareStatement(
                    "DELETE FROM \"authority\" WHERE user_id = ?"
            );                 PreparedStatement userAuthorityPs = conn.prepareStatement(
                         "UPDATE \"authority\" SET authority = ? WHERE user_id = ? AND authority = ?")) {

                userPs.setString(1, pe.encode(user.getPassword()));
                userPs.setBoolean(2, user.getEnabled());
                userPs.setBoolean(3, user.getAccountNonExpired());
                userPs.setBoolean(4, user.getAccountNonLocked());
                userPs.setBoolean(5, user.getCredentialsNonExpired());
                userPs.setString(6, user.getUsername());
                userPs.executeUpdate();

                deleteUserAuthorityPs.setObject(1, user.getId());
                deleteUserAuthorityPs.executeUpdate();

                UUID generatedUserId;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("Can't access to id");
                    }
                }
                user.setId(generatedUserId);

                for (AuthorityEntity authorityEntity : user.getAuthorities()) {
                    userAuthorityPs.setString(1, authorityEntity.getAuthority().name());
                    userAuthorityPs.setObject(2, generatedUserId);
                    userAuthorityPs.setString(3, authorityEntity.getAuthority().name());
                    userAuthorityPs.addBatch();
                    userAuthorityPs.clearParameters();
                }
                userAuthorityPs.executeBatch();
                conn.commit();

                return user;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection conn = udDataSource.getConnection()) {
            try (PreparedStatement userPs = conn.prepareStatement(
                    "UPDATE \"user\" SET currency = ?, firstname = ?, surname = ?, photo = ?, photoSmall = ?" +
                            " WHERE username = ?")) {

                userPs.setString(1, user.getCurrency().name());
                userPs.setString(2, user.getFirstname());
                userPs.setString(3, user.getSurname());
                userPs.setBytes(4, user.getPhoto());
                userPs.setBytes(5, user.getPhotoSmall());
                userPs.setString(6, user.getUsername());
                userPs.executeUpdate();
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        UserEntity user = new UserEntity();
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "SELECT * FROM \"user\" WHERE id = ?")) {
            userPs.setObject(1, id);
            userPs.execute();

            try (ResultSet resultSet = userPs.getResultSet()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setUsername(resultSet.getString("username"));
                    user.setCurrency(CurrencyValues.valueOf(resultSet.getString("currency")));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setPhoto(resultSet.getBytes("photo"));
                    user.setPhotoSmall(resultSet.getBytes("photo_small"));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(user);
    }
}
