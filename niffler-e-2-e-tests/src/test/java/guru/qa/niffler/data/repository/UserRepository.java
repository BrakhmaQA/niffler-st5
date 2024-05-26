package guru.qa.niffler.data.repository;


import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    static UserRepository getInstance() {
        if ("jdbc".equals(System.getProperty("repo"))) {
            return new UserRepositoryJdbc();
        }
        if ("spring".equals(System.getProperty("repo"))) {
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
