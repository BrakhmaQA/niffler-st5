package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.AuthorizationPage;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();

    private final MainPage mainPage = new MainPage();

    static {
        Configuration.browserSize = "1920x1080";

    }

    @BeforeEach
    void doLogin() {
        Selenide.open("http://127.0.0.1:3000/");

        authorizationPage.clickLogInButton()
                .setUsername("testuser")
                .setPassword("Voisjf%05842")
                .clickSignInButton();
    }

    @Category(
            category = "Обучение",
            username = "testuser"
    )
    @Spend(
            username = "testuser",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        mainPage.choosingFirstSpending(mainPage
                        .findSpendingByDescription(spendJson.description()))
                .clickDeleteSelected()
                .expectedTableSize(0);
    }
}
