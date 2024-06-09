package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class HeaderPage extends BasePage<HeaderPage> {
    private final SelenideElement profileAvatar = $(".header__avatar");
    private final SelenideElement friendsNavigationButton = $("li[data-tooltip-content='Friends']");
    private final SelenideElement allPeopleNavigationButton = $("li[data-tooltip-content='All people']");

    @Step("Open friends page")
    public FriendsPage openFriendsPage() {
        friendsNavigationButton.click();
        return new FriendsPage();
    }

    @Step("Open all people page")
    public AllPeoplePage openAllPeoplePage() {
        allPeopleNavigationButton.click();
        return new AllPeoplePage();
    }

    @Override
    public HeaderPage checkPageLoaded() {
        profileAvatar.shouldBe(visible);

        return this;
    }
}
