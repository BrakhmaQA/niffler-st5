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

import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITATION_RECEIVED;
import static guru.qa.niffler.jupiter.annotation.User.Selector.INVITATION_SENT;

@WebTest
public class FriendInvitationSentTest extends BaseWebTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();
    private final MainPage mainPage = new MainPage();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin(@User(selector = INVITATION_SENT) UserJson userForTest) {
        Selenide.open(CFG.frontUrl());

        authorizationPage.clickLogInButton()
                .setUsername(userForTest.username())
                .setPassword(userForTest.testData().password())
                .clickSignInButton();
    }

    @Test
    void userSentAnInvitationTest(@User(selector = INVITATION_RECEIVED) UserJson anotherUserForTest) {
        mainPage.openAllPeoplePage()
                .verifyPendingInvitationStatusFrom(anotherUserForTest.username());
    }

    @Test
    void userSentAnInvitationTest1(@User(selector = INVITATION_RECEIVED) UserJson anotherUserForTest) {
        mainPage.openAllPeoplePage()
                .verifyPendingInvitationStatusFrom(anotherUserForTest.username());
    }

    @Test
    void userSentAnInvitationTest2(@User(selector = INVITATION_RECEIVED) UserJson anotherUserForTest) {
        mainPage.openAllPeoplePage()
                .verifyPendingInvitationStatusFrom(anotherUserForTest.username());
    }
}
