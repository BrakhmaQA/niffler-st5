package guru.qa.niffler.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.Date;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {
    private final ReactCalendar reactCalendar = new ReactCalendar();
    private final Header header = new Header();
    public static final String URL = CFG.frontUrl() + "main";
    private final SelenideElement profileAvatar = $(".header__avatar");
    private final ElementsCollection table = $(".spendings-table tbody").$$("tr");
    private final SelenideElement deleteSelectedButton = $(".spendings__bulk-actions button");

    @Step("Find spending by description in table")
    public SelenideElement findSpendingByDescription(String description) {
        return table.find(text(description));
    }

    @Step("Find spending by description in table")
    public MainPage choosingFirstSpending(SelenideElement rowWithSpending) {
        rowWithSpending.$$("td").first().scrollIntoView(true).click();

        return this;
    }

    @Step("Delete selected spending")
    public MainPage clickDeleteSelected() {
        deleteSelectedButton.click();

        return this;
    }

    @Step("Verify expected table size")
    public void expectedTableSize(int sizeShouldBe) {
        table.shouldHave(size(sizeShouldBe));
    }

    @Override
    public MainPage checkPageLoaded() {
        profileAvatar.shouldBe(visible);

        return this;
    }

    @Step("Setting date in calendar")
    public MainPage setData(Date date) {
        reactCalendar.setData(date);

        return this;
    }

    @Step("Open friends page")
    public FriendsPage openFriendsPage() {
        header.openFriendsPage();

        return new FriendsPage();
    }

    @Step("Open all people page")
    public AllPeoplePage openAllPeoplePage() {
        header.openAllPeoplePage();

        return new AllPeoplePage();
    }
}
