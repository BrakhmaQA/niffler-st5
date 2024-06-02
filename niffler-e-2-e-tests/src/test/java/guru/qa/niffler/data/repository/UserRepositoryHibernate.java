package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.jpa.EmProvider;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public class UserRepositoryHibernate implements UserRepository {

    private final EntityManager authEm = EmProvider.entityManager(DataBase.AUTH);
    private final EntityManager userdataEm = EmProvider.entityManager(DataBase.USERDATA);
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        user.setPassword(pe.encode(user.getPassword()));
        authEm.persist(user);

        return user;
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        userdataEm.persist(user);

        return user;
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        authEm.merge(user);

        return user;
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        userdataEm.merge(user);

        return user;
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        return Optional.ofNullable(
                userdataEm.find(UserEntity.class, id)
        );
    }

    @Override
    public UserAuthEntity findUserFromAuthByUsername(String username) {
        return authEm.createQuery("SELECT * FROM \"authority\" WHERE username = :username", UserAuthEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public UserEntity findUserFromUserdataByUsername(String username) {
        return userdataEm.createQuery("SELECT * FROM \"user\" username = :username", UserEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
