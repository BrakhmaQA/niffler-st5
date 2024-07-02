package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.pages.AuthorizationPage;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.jupiter.annotation.User.Selector.ACCEPTED_FRIENDS;
import static guru.qa.niffler.jupiter.annotation.User.Selector.WITH_FRIENDS;

@WebTest
public class FriendsWithTest extends BaseWebTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();
    private final MainPage mainPage = new MainPage();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(selector = WITH_FRIENDS) UserJson userForTest) {
        Selenide.open(CFG.frontUrl());

        authorizationPage.clickLogInButton()
                .setUsername(userForTest.username())
                .setPassword(userForTest.testData().password())
                .clickSignInButton();
    }

    @Test
    void checkingYAFriendsStatusTest(@User(selector = ACCEPTED_FRIENDS) UserJson anotherUserForTest) {
        mainPage.openAllPeoplePage()
                .verifyYAFriendsStatusWith(anotherUserForTest.username());

        mainPage.openFriendsPage()
                .verifyYAFriendsStatusWith(anotherUserForTest.username());
    }

    @Test
    void checkingYAFriendsStatusTest1(@User(selector = ACCEPTED_FRIENDS) UserJson anotherUserForTest) {
        mainPage.openAllPeoplePage()
                .verifyYAFriendsStatusWith(anotherUserForTest.username());

        mainPage.openFriendsPage()
                .verifyYAFriendsStatusWith(anotherUserForTest.username());
    }

    @Test
    void checkingYAFriendsStatusTest2(@User(selector = ACCEPTED_FRIENDS) UserJson anotherUserForTest) {
        mainPage.openAllPeoplePage()
                .verifyYAFriendsStatusWith(anotherUserForTest.username());

        mainPage.openFriendsPage()
                .verifyYAFriendsStatusWith(anotherUserForTest.username());
    }
}
