package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import guru.qa.niffler.model.UserJson;

public class DbCreateUserExtension extends CreateUserExtension {
    private static UserRepository userRepository = UserRepository.getInstance();

    @Override
    protected UserJson createUserForTest(UserJson user) {
        userRepository.createUserInAuth(UserAuthEntity.fromJson(user));
        userRepository.createUserInUserdata(UserEntity.fromJson(user));

        return user;
    }
}
