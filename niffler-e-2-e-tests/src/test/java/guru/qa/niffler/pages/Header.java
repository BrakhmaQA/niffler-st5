package guru.qa.niffler.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {
    private final SelenideElement profileAvatar = self.$(".header__avatar");
    private final SelenideElement friendsNavigationButton = self.$("li[data-tooltip-content='Friends']");
    private final SelenideElement allPeopleNavigationButton = self.$("li[data-tooltip-content='All people']");

    protected Header(SelenideElement self) {
        super(self);
    }

    protected Header() {
        super($(".header__avatar"));
    }

    public Header openFriendsPage() {
        friendsNavigationButton.click();

        return this;
    }

    public Header openAllPeoplePage() {
        allPeopleNavigationButton.click();

        return this;
    }
}
