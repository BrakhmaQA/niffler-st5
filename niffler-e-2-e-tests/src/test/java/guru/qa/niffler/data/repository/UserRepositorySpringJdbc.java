package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.Authority;
import guru.qa.niffler.data.entity.AuthorityEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.AuthorityEntityRowMapper;
import guru.qa.niffler.data.sjdbc.UserAuthEntityRowMapper;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositorySpringJdbc implements UserRepository {
    private static final TransactionTemplate authTXTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSourceProvider.dataSource(DataBase.AUTH)
            )
    );
    private static final JdbcTemplate authJdbcTemplate = new JdbcTemplate(DataSourceProvider.dataSource(DataBase.AUTH));
    private static final JdbcTemplate udJdbcTemplate = new JdbcTemplate(DataSourceProvider.dataSource(DataBase.USERDATA));
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        return authTXTemplate.execute(status -> {
            KeyHolder kh = new GeneratedKeyHolder();

            authJdbcTemplate.update(con -> {
                        PreparedStatement ps = con.prepareStatement(
                                "INSERT INTO \"user\" (" +
                                        "username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                                        " VALUES (?, ?, ?, ?, ?, ?)",
                                PreparedStatement.RETURN_GENERATED_KEYS
                        );
                        ps.setString(1, user.getUsername());
                        ps.setString(2, pe.encode(user.getPassword()));
                        ps.setBoolean(3, user.getEnabled());
                        ps.setBoolean(4, user.getAccountNonExpired());
                        ps.setBoolean(5, user.getAccountNonLocked());
                        ps.setBoolean(6, user.getCredentialsNonExpired());

                        return ps;
                    }, kh
            );
            user.setId((UUID) kh.getKeys().get("id"));

            authJdbcTemplate.batchUpdate(
                    "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setObject(1, user.getId());
                            ps.setString(2, user.getAuthorities().get(i).getAuthority().name());
                        }

                        @Override
                        public int getBatchSize() {
                            return Authority.values().length;
                        }
                    }
            );

            return user;
        });
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();

        udJdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO \"user\" (" +
                                    "username, currency, firstname, surname, photo, photo_small)" +
                                    " VALUES (?, ?, ?, ?, ?, ?)",
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );
                    ps.setString(1, user.getUsername());
                    ps.setString(2, user.getCurrency().name());
                    ps.setString(3, user.getFirstname());
                    ps.setString(4, user.getSurname());
                    ps.setObject(5, user.getPhoto());
                    ps.setObject(6, user.getPhotoSmall());

                    return ps;
                }, kh
        );
        user.setId((UUID) kh.getKeys().get("id"));

        return user;
    }


    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();

        authJdbcTemplate.update(conn -> {
                    PreparedStatement userPs = conn.prepareStatement("UPDATE \"user\" SET " +
                                    "password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ?" +
                                    " WHERE username = ?",
                            PreparedStatement.RETURN_GENERATED_KEYS);
                    PreparedStatement userAuthorityPs = conn.prepareStatement(
                            "UPDATE \"authority\" SET authority = ? WHERE user_id = ?");

                    userPs.setString(1, pe.encode(user.getPassword()));
                    userPs.setBoolean(2, user.getEnabled());
                    userPs.setBoolean(3, user.getAccountNonExpired());
                    userPs.setBoolean(4, user.getAccountNonLocked());
                    userPs.setBoolean(5, user.getCredentialsNonExpired());
                    userPs.setString(6, user.getUsername());
                    userPs.executeUpdate();

                    for (AuthorityEntity authorityEntity : user.getAuthorities()) {
                        userAuthorityPs.setString(1, authorityEntity.getAuthority().name());
                        userAuthorityPs.setObject(2, kh.getKeys().get("id"));
                        userAuthorityPs.addBatch();
                        userAuthorityPs.clearParameters();
                    }
                    userAuthorityPs.executeBatch();

                    return userPs;
                }, kh
        );

        return user;
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        udJdbcTemplate.update(
                "UPDATE \"user\" SET currency = ?, firstname = ?, surname = ?, photo = ?" +
                        " WHERE username = ?",
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getUsername()
        );

        return user;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        try {
            return Optional.of(udJdbcTemplate.queryForObject(
                    "SELECT * FROM \"user\" WHERE id = ?",
                    UserEntityRowMapper.instance,
                    id
            ));
        } catch (DataRetrievalFailureException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserAuthEntity findUserFromAuthByUsername(String username) {
        UserAuthEntity userAuthEntity = authJdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE username = ?",
                UserAuthEntityRowMapper.instance,
                username
        );

        List<AuthorityEntity> authorityEntities = authJdbcTemplate.query(
                "SELECT * FROM \"authority\" WHERE user_id = ?",
                AuthorityEntityRowMapper.instance,
                userAuthEntity.getId()
        );

        userAuthEntity.setAuthorities(authorityEntities);

        return userAuthEntity;
    }

    @Override
    public UserEntity findUserFromUserdataByUsername(String username) {
        return udJdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE username = ?",
                UserEntityRowMapper.instance,
                username
        );
    }
}
