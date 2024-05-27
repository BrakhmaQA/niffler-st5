package guru.qa.niffler.data.repository;


import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    static UserRepository getInstance() {
        String repo = System.getProperty("repo");

        if ("jdbc".equals(repo)) {
            return new UserRepositoryJdbc();
        }
        if ("spring".equals(repo)) {
            return new UserRepositorySpringJdbc();
        }

        return new UserRepositoryJdbc();
    }

    UserAuthEntity createUserInAuth(UserAuthEntity user);

    UserEntity createUserInUserdata(UserEntity user);

    UserAuthEntity updateUserInAuth(UserAuthEntity category);

    UserEntity updateUserInUserdata(UserEntity category);

    Optional<UserEntity> findUserInUserdataById(UUID id);
}
