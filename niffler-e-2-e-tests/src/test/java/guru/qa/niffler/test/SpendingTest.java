package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spend;
import guru.qa.niffler.jupiter.annotation.Spends;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.AuthorizationPage;
import guru.qa.niffler.pages.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.SpendsCondition.spendsInTable;

@WebTest
public class SpendingTest extends BaseWebTest {
    private final AuthorizationPage authorizationPage = new AuthorizationPage();

    private final MainPage mainPage = new MainPage();

    static {
        Configuration.browserSize = "1920x1080";

    }

    @BeforeEach
    void doLogin() {
        Selenide.open(CFG.frontUrl());

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

    @Category(
            category = "Обучение - 1",
            username = "testuser"
    )
    @Spends({
            @Spend(
                    username = "testuser",
                    description = "QA.GURU Advanced 5",
                    amount = 65000.00,
                    currency = CurrencyValues.RUB,
                    category = "Обучение - 1"
            ),
            @Spend(
                    username = "testuser1",
                    description = "QA.GURU Advanced 5",
                    amount = 30000.00,
                    currency = CurrencyValues.RUB,
                    category = "Обучение - 1"
            )
    })
    @Test
    void verifySpendingContent(SpendJson[] spends) {
        ElementsCollection spendings = $(".spendings-table tbody")
                .$$("tr");

        spendings.get(0).scrollIntoView(true);
        spendings.shouldHave(spendsInTable(spends));
    }
}
